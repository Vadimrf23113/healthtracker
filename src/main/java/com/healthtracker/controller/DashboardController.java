package com.healthtracker.controller;

import com.healthtracker.model.HealthRecord;
import com.healthtracker.service.AppState;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.time.LocalDate;

public class DashboardController {

    @FXML private DatePicker datePicker;

    @FXML private TextField weightField;
    @FXML private TextField waterMlField;
    @FXML private TextField sleepHoursField;

    @FXML private TextArea notesArea;

    @FXML private Label recordsCountLabel;
    @FXML private Label lastSavedLabel;

    private final AppState appState = AppState.getInstance();

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());

        appState.getRecords().addListener((javafx.collections.ListChangeListener<HealthRecord>) c -> refreshMiniStats());
        refreshMiniStats();

        notesArea.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.S) {
                onQuickSave();
                e.consume();
            }
        });
    }

    @FXML
    private void onAddOrUpdateToday() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            warn("Дата не выбрана", "Выберите дату.");
            return;
        }

        Double weight = parseDoubleOrNull(weightField.getText());
        Integer water = parseIntOrNull(waterMlField.getText());
        Double sleep = parseDoubleOrNull(sleepHoursField.getText());
        String notes = notesArea.getText() == null ? "" : notesArea.getText().trim();

        // Пример валидации
        if (weight != null && (weight <= 0 || weight > 400)) {
            warn("Некорректный вес", "Укажите вес в разумных пределах.");
            return;
        }
        if (water != null && (water < 0 || water > 20000)) {
            warn("Некорректная вода", "Укажите воду в мл (0..20000).");
            return;
        }
        if (sleep != null && (sleep < 0 || sleep > 24)) {
            warn("Некорректный сон", "Укажите сон в часах (0..24).");
            return;
        }

        appState.upsertDailyRecord(date, weight, water, sleep, notes);

        info("Готово", "Запись за выбранную дату добавлена/обновлена.");
        refreshMiniStats();
    }

    @FXML
    private void onFillFromSelectedDate() {
        LocalDate date = datePicker.getValue();
        if (date == null) return;

        HealthRecord r = appState.findByDate(date);
        if (r == null) {
            weightField.clear();
            waterMlField.clear();
            sleepHoursField.clear();
            notesArea.clear();
            return;
        }

        weightField.setText(r.getWeightKg() == null ? "" : String.valueOf(r.getWeightKg()));
        waterMlField.setText(r.getWaterMl() == null ? "" : String.valueOf(r.getWaterMl()));
        sleepHoursField.setText(r.getSleepHours() == null ? "" : String.valueOf(r.getSleepHours()));
        notesArea.setText(r.getNotes() == null ? "" : r.getNotes());
    }

    @FXML
    private void onQuickSave() {
        try {
            appState.save();
            lastSavedLabel.setText("Сохранено: сейчас");
        } catch (Exception e) {
            error("Ошибка сохранения", e.getMessage());
        }
    }

    private void refreshMiniStats() {
        recordsCountLabel.setText("Записей: " + appState.getRecords().size());
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try { return Double.parseDouble(t.replace(',', '.')); }
        catch (NumberFormatException e) { return null; }
    }

    private Integer parseIntOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try { return Integer.parseInt(t); }
        catch (NumberFormatException e) { return null; }
    }

    private void warn(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(content);
        a.showAndWait();
    }

    private void info(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }

    private void error(String title, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(content);
        a.showAndWait();
    }
}