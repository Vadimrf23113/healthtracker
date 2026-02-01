package com.healthtracker.controller;

import com.healthtracker.config.AppConstants;
import com.healthtracker.service.AppState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RootController {

    private final AppState appState = AppState.getInstance();

    @FXML
    private void initialize() {
        try {
            appState.load();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Загрузка");
            a.setHeaderText("Не удалось загрузить данные");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void onSave() {
        try {
            appState.save();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Сохранение");
            a.setHeaderText(null);
            a.setContentText("Данные сохранены.");
            a.showAndWait();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Сохранение");
            a.setHeaderText("Ошибка сохранения");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void onLoad() {
        Optional<ButtonType> res = confirm("Загрузка", "Загрузить данные из файла?", "Текущие данные в окне могут измениться.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        try {
            appState.load();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Загрузка");
            a.setHeaderText(null);
            a.setContentText("Данные загружены.");
            a.showAndWait();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Загрузка");
            a.setHeaderText("Ошибка загрузки");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void onExit() {
        Optional<ButtonType> res = confirm("Выход", "Выйти из приложения?", "Несохранённые изменения будут потеряны.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        Platform.exit();
    }

    @FXML
    private void onProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstants.PROFILE_DIALOG_VIEW));
            Parent root = loader.load();

            ProfileDialogController controller = loader.getController();
            controller.setAppState(appState);

            Stage owner = getOwnerStage();
            Stage stage = new Stage();
            stage.setTitle("Профиль");
            stage.initModality(Modality.APPLICATION_MODAL);
            if (owner != null) stage.initOwner(owner);

            Scene scene = new Scene(root);
            applyCss(scene);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            error("Профиль", e.getMessage());
        }
    }

    @FXML
    private void onAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstants.ABOUT_DIALOG_VIEW));
            Parent root = loader.load();

            Stage owner = getOwnerStage();
            Stage stage = new Stage();
            stage.setTitle("О программе");
            stage.initModality(Modality.APPLICATION_MODAL);
            if (owner != null) stage.initOwner(owner);

            Scene scene = new Scene(root);
            applyCss(scene);
            stage.setScene(scene);
            stage.setResizable(false);

            AboutController controller = loader.getController();
            controller.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            error("О программе", e.getMessage());
        }
    }

    private Stage getOwnerStage() {
        for (Stage s : Stage.getWindows().stream().filter(w -> w instanceof Stage).map(w -> (Stage) w).toList()) {
            if (s.isShowing()) return s;
        }
        return null;
    }

    private void applyCss(Scene scene) {
        try {
            String css = getClass().getResource(AppConstants.MAIN_CSS).toExternalForm();
            if (!scene.getStylesheets().contains(css)) scene.getStylesheets().add(css);
        } catch (Exception ignored) {
        }
    }

    private Optional<ButtonType> confirm(String title, String header, String content) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        return a.showAndWait();
    }

    private void error(String title, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(content);
        a.showAndWait();
    }
}