package com.healthtracker.service;

import com.google.gson.*;
import com.healthtracker.config.AppConstants;
import com.healthtracker.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageService {

    private final Path filePath;
    private final Gson gson;

    public JsonStorageService() {
        this(Paths.get(AppConstants.DATA_FILE_NAME));
    }

    public JsonStorageService(Path filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }

    public void save(AppStateSnapshot snapshot) throws IOException {
        ensureParentDir();
        try (BufferedWriter w = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            StorageDto dto = toDto(snapshot);
            gson.toJson(dto, w);
        }
    }

    public AppStateSnapshot load() throws IOException {
        if (Files.exists(filePath)) {
            try (BufferedReader r = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
                StorageDto dto = gson.fromJson(r, StorageDto.class);
                return fromDto(dto);
            }
        }

        StorageDto dtoFromResource = loadFromResource("/com/healthtracker/data/healthtracker.json");
        if (dtoFromResource != null) {
            return fromDto(dtoFromResource);
        }

        return new AppStateSnapshot(new UserProfile(), new ArrayList<>());
    }

    public Path getFilePath() {
        return filePath;
    }

    private void ensureParentDir() throws IOException {
        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private StorageDto loadFromResource(String resourcePath) throws IOException {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        if (in == null) return null;

        try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return gson.fromJson(r, StorageDto.class);
        }
    }

    private StorageDto toDto(AppStateSnapshot snapshot) {
        StorageDto dto = new StorageDto();
        dto.profile = profileToDto(snapshot.profile());

        dto.records = new ArrayList<>();
        for (HealthRecord r : snapshot.records()) {
            dto.records.add(recordToDto(r));
        }
        return dto;
    }

    private AppStateSnapshot fromDto(StorageDto dto) {
        if (dto == null) {
            return new AppStateSnapshot(new UserProfile(), new ArrayList<>());
        }

        UserProfile profile = dto.profile == null
                ? new UserProfile()
                : new UserProfile(dto.profile.name, dto.profile.age, dto.profile.heightCm, dto.profile.goal);

        List<HealthRecord> records = new ArrayList<>();
        if (dto.records != null) {
            for (RecordDto rd : dto.records) {
                HealthRecord r = new HealthRecord(rd.id, rd.date);
                r.setWeightKg(rd.weightKg);
                r.setWaterMl(rd.waterMl);
                r.setSleepHours(rd.sleepHours);
                r.setNotes(rd.notes);

                if (rd.meals != null) {
                    for (MealDto md : rd.meals) {
                        MealEntry m = new MealEntry(md.id, md.type, md.time, md.calories, md.description);
                        r.getMeals().add(m);
                    }
                }

                if (rd.activities != null) {
                    for (ActivityDto ad : rd.activities) {
                        ActivityEntry a = new ActivityEntry(ad.id, ad.type, ad.time, ad.durationMin, ad.caloriesBurned, ad.notes);
                        r.getActivities().add(a);
                    }
                }

                records.add(r);
            }
        }

        return new AppStateSnapshot(profile, records);
    }

    private ProfileDto profileToDto(UserProfile p) {
        ProfileDto dto = new ProfileDto();
        dto.name = p.getName();
        dto.age = p.getAge();
        dto.heightCm = p.getHeightCm();
        dto.goal = p.getGoal();
        return dto;
    }

    private RecordDto recordToDto(HealthRecord r) {
        RecordDto dto = new RecordDto();
        dto.id = r.getId();
        dto.date = r.getDate();
        dto.weightKg = r.getWeightKg();
        dto.waterMl = r.getWaterMl();
        dto.sleepHours = r.getSleepHours();
        dto.notes = r.getNotes();

        dto.meals = new ArrayList<>();
        for (MealEntry m : r.getMeals()) {
            dto.meals.add(mealToDto(m));
        }

        dto.activities = new ArrayList<>();
        for (ActivityEntry a : r.getActivities()) {
            dto.activities.add(activityToDto(a));
        }

        return dto;
    }

    private MealDto mealToDto(MealEntry m) {
        MealDto dto = new MealDto();
        dto.id = m.getId();
        dto.type = m.getType();
        dto.time = m.getTime();
        dto.calories = m.getCalories();
        dto.description = m.getDescription();
        return dto;
    }

    private ActivityDto activityToDto(ActivityEntry a) {
        ActivityDto dto = new ActivityDto();
        dto.id = a.getId();
        dto.type = a.getType();
        dto.time = a.getTime();
        dto.durationMin = a.getDurationMin();
        dto.caloriesBurned = a.getCaloriesBurned();
        dto.notes = a.getNotes();
        return dto;
    }

    public record AppStateSnapshot(UserProfile profile, List<HealthRecord> records) {
    }

    private static final class StorageDto {
        ProfileDto profile;
        List<RecordDto> records;
    }

    private static final class ProfileDto {
        String name;
        Integer age;
        Integer heightCm;
        String goal;
    }

    private static final class RecordDto {
        String id;
        LocalDate date;
        Double weightKg;
        Integer waterMl;
        Double sleepHours;
        String notes;
        List<MealDto> meals;
        List<ActivityDto> activities;
    }

    private static final class MealDto {
        String id;
        MealType type;
        LocalTime time;
        Integer calories;
        String description;
    }

    private static final class ActivityDto {
        String id;
        ActivityType type;
        LocalTime time;
        Integer durationMin;
        Integer caloriesBurned;
        String notes;
    }

    private static final class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            if (json == null || json.isJsonNull()) return null;
            String s = json.getAsString();
            return (s == null || s.isBlank()) ? null : LocalDate.parse(s);
        }
    }

    private static final class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        @Override
        public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
        }

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            if (json == null || json.isJsonNull()) return null;
            String s = json.getAsString();
            return (s == null || s.isBlank()) ? null : LocalTime.parse(s);
        }
    }
}