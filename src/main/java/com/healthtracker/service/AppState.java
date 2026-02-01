package com.healthtracker.service;

import com.healthtracker.model.HealthRecord;
import com.healthtracker.model.UserProfile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public final class AppState {

    private static final AppState INSTANCE = new AppState();

    private final ObservableList<HealthRecord> records = FXCollections.observableArrayList();
    private UserProfile profile = new UserProfile();

    private final JsonStorageService storageService = new JsonStorageService();

    private AppState() {
    }

    public static AppState getInstance() {
        return INSTANCE;
    }

    public ObservableList<HealthRecord> getRecords() {
        return records;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void updateProfile(String name, Integer age, Integer heightCm, String goal) {
        if (profile == null) profile = new UserProfile();
        profile.setName(name);
        profile.setAge(age);
        profile.setHeightCm(heightCm);
        profile.setGoal(goal);
    }

    public HealthRecord findByDate(LocalDate date) {
        if (date == null) return null;
        for (HealthRecord r : records) {
            if (date.equals(r.getDate())) return r;
        }
        return null;
    }

    public void upsertDailyRecord(LocalDate date, Double weightKg, Integer waterMl, Double sleepHours, String notes) {
        if (date == null) return;

        HealthRecord existing = findByDate(date);
        if (existing == null) {
            HealthRecord r = new HealthRecord(IdGenerator.newId(), date);
            r.setWeightKg(weightKg);
            r.setWaterMl(waterMl);
            r.setSleepHours(sleepHours);
            r.setNotes(notes);
            records.add(r);
        } else {
            existing.setWeightKg(weightKg);
            existing.setWaterMl(waterMl);
            existing.setSleepHours(sleepHours);
            existing.setNotes(notes);
        }

        sortByDateDesc();
    }

    public void updateRecord(HealthRecord target, LocalDate newDate, Double weightKg, Integer waterMl, Double sleepHours, String notes) {
        if (target == null) return;
        if (newDate == null) return;

        if (!newDate.equals(target.getDate())) {
            HealthRecord other = findByDate(newDate);
            if (other != null && other != target) {
                other.setWeightKg(weightKg);
                other.setWaterMl(waterMl);
                other.setSleepHours(sleepHours);
                other.setNotes(notes);
                return;
            }
        }

        target.setDate(newDate);
        target.setWeightKg(weightKg);
        target.setWaterMl(waterMl);
        target.setSleepHours(sleepHours);
        target.setNotes(notes);

        sortByDateDesc();
    }

    public void deleteRecord(HealthRecord record) {
        if (record == null) return;
        records.remove(record);
    }

    public void save() throws IOException {
        storageService.save(new JsonStorageService.AppStateSnapshot(profile, List.copyOf(records)));
    }

    public void load() throws IOException {
        JsonStorageService.AppStateSnapshot snap = storageService.load();
        this.profile = snap.profile() == null ? new UserProfile() : snap.profile();
        records.setAll(snap.records() == null ? List.of() : snap.records());
        sortByDateDesc();
    }

    private void sortByDateDesc() {
        FXCollections.sort(records, Comparator.comparing(HealthRecord::getDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
    }
}