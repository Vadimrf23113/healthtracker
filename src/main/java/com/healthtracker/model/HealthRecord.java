package com.healthtracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Objects;

public class HealthRecord {

    private String id;
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> weightKg = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> waterMl = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> sleepHours = new SimpleObjectProperty<>();
    private final StringProperty notes = new SimpleStringProperty("");

    private final ObservableList<MealEntry> meals = FXCollections.observableArrayList();
    private final ObservableList<ActivityEntry> activities = FXCollections.observableArrayList();

    public HealthRecord() {
    }

    public HealthRecord(String id, LocalDate date) {
        this.id = id;
        this.date.set(date);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public Double getWeightKg() {
        return weightKg.get();
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg.set(weightKg);
    }

    public ObjectProperty<Double> weightKgProperty() {
        return weightKg;
    }

    public Integer getWaterMl() {
        return waterMl.get();
    }

    public void setWaterMl(Integer waterMl) {
        this.waterMl.set(waterMl);
    }

    public ObjectProperty<Integer> waterMlProperty() {
        return waterMl;
    }

    public Double getSleepHours() {
        return sleepHours.get();
    }

    public void setSleepHours(Double sleepHours) {
        this.sleepHours.set(sleepHours);
    }

    public ObjectProperty<Double> sleepHoursProperty() {
        return sleepHours;
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

    public ObservableList<MealEntry> getMeals() {
        return meals;
    }

    public ObservableList<ActivityEntry> getActivities() {
        return activities;
    }

    public int totalMealCalories() {
        int sum = 0;
        for (MealEntry m : meals) {
            if (m != null && m.getCalories() != null) sum += m.getCalories();
        }
        return sum;
    }

    public int totalActivityCaloriesBurned() {
        int sum = 0;
        for (ActivityEntry a : activities) {
            if (a != null && a.getCaloriesBurned() != null) sum += a.getCaloriesBurned();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthRecord that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}