package com.healthtracker.controller;

import com.healthtracker.config.AppConstants;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController {

    @FXML private Label appNameLabel;
    @FXML private Label versionLabel;
    @FXML private Label detailsLabel;
    @FXML private Button closeButton;

    private Stage stage;

    @FXML
    private void initialize() {
        appNameLabel.setText(AppConstants.APP_NAME);
        versionLabel.setText("Версия: " + AppConstants.APP_VERSION);
        detailsLabel.setText(
                "HealthTracker — приложение для мониторинга здоровья.\n\n" +
                        "Функции:\n" +
                        "• ежедневные записи (вес, вода, сон, заметки)\n" +
                        "• поиск и фильтрация\n" +
                        "• мини-статистика и средние значения\n" +
                        "• сохранение/загрузка в JSON\n\n" +
                        "Проект: JavaFX"
        );
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onClose() {
        if (stage != null) {
            stage.close();
            return;
        }
        Stage s = (Stage) closeButton.getScene().getWindow();
        s.close();
    }
}