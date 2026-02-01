package com.healthtracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class UserProfile {

    private final StringProperty name = new SimpleStringProperty("");
    private final ObjectProperty<Integer> age = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> heightCm = new SimpleObjectProperty<>();
    private final StringProperty goal = new SimpleStringProperty("");

    public UserProfile() {
    }

    public UserProfile(String name, Integer age, Integer heightCm, String goal) {
        this.name.set(name == null ? "" : name);
        this.age.set(age);
        this.heightCm.set(heightCm);
        this.goal.set(goal == null ? "" : goal);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name == null ? "" : name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Integer getAge() {
        return age.get();
    }

    public void setAge(Integer age) {
        this.age.set(age);
    }

    public ObjectProperty<Integer> ageProperty() {
        return age;
    }

    public Integer getHeightCm() {
        return heightCm.get();
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm.set(heightCm);
    }

    public ObjectProperty<Integer> heightCmProperty() {
        return heightCm;
    }

    public String getGoal() {
        return goal.get();
    }

    public void setGoal(String goal) {
        this.goal.set(goal == null ? "" : goal);
    }

    public StringProperty goalProperty() {
        return goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getAge(), that.getAge())
                && Objects.equals(getHeightCm(), that.getHeightCm())
                && Objects.equals(getGoal(), that.getGoal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getHeightCm(), getGoal());
    }
}