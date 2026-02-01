package com.healthtracker.controller;

import com.healthtracker.model.HealthRecord;
import com.healthtracker.service.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RecordDialogController {

    @FXML private DatePicker datePicker;

    @FXML private TextField weightField;
    @FXML private TextField waterMlField;
    @FXML private TextField sleepHoursField;
    @FXML private TextArea notesArea;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private AppState appState;
    private HealthRecord editTarget; // null => create

    public void setAppState(AppState appState) {
        this.appState = appState;
    }

    public void setEditTarget(HealthRecord record) {
        this.editTarget = record;
        fillForm();
    }

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    private void fillForm() {
        if (editTarget == null) return;

        datePicker.setValue(editTarget.getDate());
        weightField.setText(editTarget.getWeightKg() == null ? "" : String.valueOf(editTarget.getWeightKg()));
        waterMlField.setText(editTarget.getWaterMl() == null ? "" : String.valueOf(editTarget.getWaterMl()));
        sleepHoursField.setText(editTarget.getSleepHours() == null ? "" : String.valueOf(editTarget.getSleepHours()));
        notesArea.setText(editTarget.getNotes() == null ? "" : editTarget.getNotes());
    }

    @FXML
    private void onSave() {
        if (appState == null) {
            error("Внутренняя ошибка", "AppState не установлен в контроллере.");
            return;
        }

        LocalDate date = datePicker.getValue();
        if (date == null) {
            warn("Валидация", "Дата обязательна.");
            return;
        }

        Double weight = parseDoubleOrNull(weightField.getText());
        Integer water = parseIntOrNull(waterMlField.getText());
        Double sleep = parseDoubleOrNull(sleepHoursField.getText());
        String notes = notesArea.getText() == null ? "" : notesArea.getText().trim();

        // Валидация
        if (weight != null && (weight <= 0 || weight > 400)) {
            warn("Валидация", "Вес должен быть в пределах 1..400 кг.");
            return;
        }
        if (water != null && (water < 0 || water > 20000)) {
            warn("Валидация", "Вода (мл) должна быть в пределах 0..20000.");
            return;
        }
        if (sleep != null && (sleep < 0 || sleep > 24)) {
            warn("Валидация", "Сон (часы) должен быть в пределах 0..24.");
            return;
        }

        if (editTarget == null) {
            // Create
            appState.upsertDailyRecord(date, weight, water, sleep, notes);
        } else {
            // Update (через upsert по дате или напрямую)
            appState.updateRecord(editTarget, date, weight, water, sleep, notes);
        }

        close();
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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

    private void error(String title, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(content);
        a.showAndWait();
    }
}