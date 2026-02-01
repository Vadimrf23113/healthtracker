package com.healthtracker.service;

import com.healthtracker.model.HealthRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatsService {

    private final AppState appState;

    public StatsService(AppState appState) {
        this.appState = appState;
    }

    public Double averageWeight() {
        List<Double> values = new ArrayList<>();
        for (HealthRecord r : appState.getRecords()) {
            if (r.getWeightKg() != null) values.add(r.getWeightKg());
        }
        return avg(values);
    }

    public Double averageSleep() {
        List<Double> values = new ArrayList<>();
        for (HealthRecord r : appState.getRecords()) {
            if (r.getSleepHours() != null) values.add(r.getSleepHours());
        }
        return avg(values);
    }

    public Double averageWater() {
        List<Double> values = new ArrayList<>();
        for (HealthRecord r : appState.getRecords()) {
            if (r.getWaterMl() != null) values.add(r.getWaterMl().doubleValue());
        }
        return avg(values);
    }

    public String last7DaysSummary() {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(6);

        int days = 0;
        int waterDays = 0;
        int sleepDays = 0;

        double waterSum = 0;
        double sleepSum = 0;

        for (HealthRecord r : appState.getRecords()) {
            LocalDate d = r.getDate();
            if (d == null) continue;
            if (d.isBefore(from) || d.isAfter(today)) continue;

            days++;

            if (r.getWaterMl() != null) {
                waterDays++;
                waterSum += r.getWaterMl();
            }

            if (r.getSleepHours() != null) {
                sleepDays++;
                sleepSum += r.getSleepHours();
            }
        }

        String avgWater = waterDays == 0 ? "—" : String.format("%.0f мл", waterSum / waterDays);
        String avgSleep = sleepDays == 0 ? "—" : String.format("%.1f ч", sleepSum / sleepDays);

        return "7 дней: записей " + days + ", средняя вода " + avgWater + ", средний сон " + avgSleep;
    }

    private Double avg(List<Double> values) {
        if (values == null || values.isEmpty()) return null;
        double sum = 0;
        for (Double v : values) sum += v;
        return sum / values.size();
    }
}