package com.healthtracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalTime;
import java.util.Objects;

public class ActivityEntry {

    private String id;

    private final ObjectProperty<ActivityType> type = new SimpleObjectProperty<>(ActivityType.WALKING);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(LocalTime.now());
    private final ObjectProperty<Integer> durationMin = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> caloriesBurned = new SimpleObjectProperty<>();
    private final StringProperty notes = new SimpleStringProperty("");

    public ActivityEntry() {
    }

    public ActivityEntry(String id) {
        this.id = id;
    }

    public ActivityEntry(String id, ActivityType type, LocalTime time, Integer durationMin, Integer caloriesBurned, String notes) {
        this.id = id;
        this.type.set(type == null ? ActivityType.WALKING : type);
        this.time.set(time == null ? LocalTime.now() : time);
        this.durationMin.set(durationMin);
        this.caloriesBurned.set(caloriesBurned);
        this.notes.set(notes == null ? "" : notes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActivityType getType() {
        return type.get();
    }

    public void setType(ActivityType type) {
        this.type.set(type == null ? ActivityType.WALKING : type);
    }

    public ObjectProperty<ActivityType> typeProperty() {
        return type;
    }

    public LocalTime getTime() {
        return time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time == null ? LocalTime.now() : time);
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public Integer getDurationMin() {
        return durationMin.get();
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin.set(durationMin);
    }

    public ObjectProperty<Integer> durationMinProperty() {
        return durationMin;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned.get();
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned.set(caloriesBurned);
    }

    public ObjectProperty<Integer> caloriesBurnedProperty() {
        return caloriesBurned;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes == null ? "" : notes);
    }

    public StringProperty notesProperty() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityEntry that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}