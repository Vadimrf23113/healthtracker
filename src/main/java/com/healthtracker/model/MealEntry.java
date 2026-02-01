package com.healthtracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalTime;
import java.util.Objects;

public class MealEntry {

    private String id;

    private final ObjectProperty<MealType> type = new SimpleObjectProperty<>(MealType.BREAKFAST);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(LocalTime.now());
    private final ObjectProperty<Integer> calories = new SimpleObjectProperty<>();
    private final StringProperty description = new SimpleStringProperty("");

    public MealEntry() {
    }

    public MealEntry(String id) {
        this.id = id;
    }

    public MealEntry(String id, MealType type, LocalTime time, Integer calories, String description) {
        this.id = id;
        this.type.set(type == null ? MealType.BREAKFAST : type);
        this.time.set(time == null ? LocalTime.now() : time);
        this.calories.set(calories);
        this.description.set(description == null ? "" : description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MealType getType() {
        return type.get();
    }

    public void setType(MealType type) {
        this.type.set(type == null ? MealType.BREAKFAST : type);
    }

    public ObjectProperty<MealType> typeProperty() {
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

    public Integer getCalories() {
        return calories.get();
    }

    public void setCalories(Integer calories) {
        this.calories.set(calories);
    }

    public ObjectProperty<Integer> caloriesProperty() {
        return calories;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description == null ? "" : description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealEntry mealEntry)) return false;
        return Objects.equals(id, mealEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}