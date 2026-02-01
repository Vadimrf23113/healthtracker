package com.healthtracker.controller;

import com.healthtracker.model.HealthRecord;
import com.healthtracker.service.AppState;
import com.healthtracker.service.StatsService;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController {

    @FXML private Label totalRecordsLabel;
    @FXML private Label avgWeightLabel;
    @FXML private Label avgSleepLabel;
    @FXML private Label avgWaterLabel;
    @FXML private Label last7DaysLabel;

    private final AppState appState = AppState.getInstance();
    private final StatsService statsService = new StatsService(appState);

    @FXML
    private void initialize() {
        appState.getRecords().addListener((ListChangeListener<HealthRecord>) c -> refresh());
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        totalRecordsLabel.setText(String.valueOf(appState.getRecords().size()));

        Double avgWeight = statsService.averageWeight();
        Double avgSleep = statsService.averageSleep();
        Double avgWater = statsService.averageWater();

        avgWeightLabel.setText(avgWeight == null ? "—" : String.format("%.1f кг", avgWeight));
        avgSleepLabel.setText(avgSleep == null ? "—" : String.format("%.1f ч", avgSleep));
        avgWaterLabel.setText(avgWater == null ? "—" : String.format("%.0f мл", avgWater));

        last7DaysLabel.setText(statsService.last7DaysSummary());
    }
}