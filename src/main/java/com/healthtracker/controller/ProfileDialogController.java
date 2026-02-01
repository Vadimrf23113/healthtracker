package com.healthtracker.controller;

import com.healthtracker.config.AppConstants;
import com.healthtracker.service.AppState;
import com.healthtracker.util.Validators;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileDialogController {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField heightCmField;
    @FXML private ChoiceBox<String> goalChoice;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private AppState appState;

    @FXML
    private void initialize() {
        goalChoice.getItems().setAll(
                "Поддержание формы",
                "Похудение",
                "Набор массы",
                "Здоровые привычки",
                "Улучшение сна",
                "Другое"
        );
        goalChoice.getSelectionModel().selectFirst();
    }

    public void setAppState(AppState appState) {
        this.appState = appState;
        if (appState == null || appState.getProfile() == null) return;

        nameField.setText(appState.getProfile().getName());
        ageField.setText(appState.getProfile().getAge() == null ? "" : String.valueOf(appState.getProfile().getAge()));
        heightCmField.setText(appState.getProfile().getHeightCm() == null ? "" : String.valueOf(appState.getProfile().getHeightCm()));

        String goal = appState.getProfile().getGoal();
        if (goal != null && !goal.isBlank()) {
            if (!goalChoice.getItems().contains(goal)) goalChoice.getItems().add(goal);
            goalChoice.getSelectionModel().select(goal);
        }
    }

    @FXML
    private void onSave() {
        if (appState == null) return;

        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        Integer age = Validators.parseIntOrNull(ageField.getText());
        Integer height = Validators.parseIntOrNull(heightCmField.getText());
        String goal = goalChoice.getValue();

        if (!Validators.notBlank(name)) {
            warn("Проверь ввод", "Имя не должно быть пустым.");
            return;
        }
        if (age != null && !Validators.inRange(age, AppConstants.MIN_AGE, AppConstants.MAX_AGE)) {
            warn("Проверь ввод", "Возраст должен быть от " + AppConstants.MIN_AGE + " до " + AppConstants.MAX_AGE + ".");
            return;
        }
        if (height != null && !Validators.inRange(height, AppConstants.MIN_HEIGHT_CM, AppConstants.MAX_HEIGHT_CM)) {
            warn("Проверь ввод", "Рост должен быть от " + AppConstants.MIN_HEIGHT_CM + " до " + AppConstants.MAX_HEIGHT_CM + " см.");
            return;
        }

        appState.updateProfile(name, age, height, goal);

        try {
            appState.save();
        } catch (Exception ignored) {
        }

        close();
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }

    private void warn(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(content);
        a.showAndWait();
    }
}