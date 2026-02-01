package com.healthtracker.controller;

import com.healthtracker.model.HealthRecord;
import com.healthtracker.service.AppState;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class RecordsController {

    @FXML private TableView<HealthRecord> table;
    @FXML private TableColumn<HealthRecord, LocalDate> dateCol;
    @FXML private TableColumn<HealthRecord, Double> weightCol;
    @FXML private TableColumn<HealthRecord, Integer> waterCol;
    @FXML private TableColumn<HealthRecord, Double> sleepCol;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> quickFilterCombo;
    @FXML private Label countLabel;

    private final AppState appState = AppState.getInstance();
    private FilteredList<HealthRecord> filtered;

    @FXML
    private void initialize() {
        dateCol.setCellValueFactory(cd -> cd.getValue().dateProperty());
        weightCol.setCellValueFactory(cd -> cd.getValue().weightKgProperty());
        waterCol.setCellValueFactory(cd -> cd.getValue().waterMlProperty());
        sleepCol.setCellValueFactory(cd -> cd.getValue().sleepHoursProperty());

        filtered = new FilteredList<>(appState.getRecords(), r -> true);

        searchField.textProperty().addListener((obs, old, val) -> applyFilters());

        quickFilterCombo.getItems().setAll("Все", "Есть вес", "Есть вода", "Есть сон", "Есть заметка");
        quickFilterCombo.getSelectionModel().selectFirst();
        quickFilterCombo.valueProperty().addListener((obs, old, val) -> applyFilters());

        SortedList<HealthRecord> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        appState.getRecords().addListener((ListChangeListener<HealthRecord>) c -> refreshCount());
        refreshCount();

        table.setRowFactory(tv -> {
            TableRow<HealthRecord> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && !row.isEmpty()) {
                    onEdit();
                }
            });
            return row;
        });
    }

    @FXML
    private void onAdd() {
        openRecordDialog(null);
    }

    @FXML
    private void onEdit() {
        HealthRecord selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            warn("Не выбрано", "Выберите запись в таблице.");
            return;
        }
        openRecordDialog(selected);
    }

    @FXML
    private void onDelete() {
        HealthRecord selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            warn("Не выбрано", "Выберите запись для удаления.");
            return;
        }

        Optional<ButtonType> res = confirm("Удаление", "Удалить запись?", "Действие нельзя отменить.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        appState.deleteRecord(selected);
    }

    @FXML
    private void onClearSearch() {
        searchField.clear();
        quickFilterCombo.getSelectionModel().selectFirst();
        applyFilters();
    }

    private void applyFilters() {
        String text = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String mode = quickFilterCombo.getValue();

        filtered.setPredicate(r -> {
            if (r == null) return false;

            boolean matchesText = true;
            if (!text.isEmpty()) {
                String hay = (r.getNotes() == null ? "" : r.getNotes()).toLowerCase();
                String date = r.getDate() == null ? "" : r.getDate().toString();
                matchesText = hay.contains(text) || date.contains(text);
            }

            boolean matchesMode = true;
            if ("Есть вес".equals(mode)) matchesMode = r.getWeightKg() != null;
            else if ("Есть вода".equals(mode)) matchesMode = r.getWaterMl() != null;
            else if ("Есть сон".equals(mode)) matchesMode = r.getSleepHours() != null;
            else if ("Есть заметка".equals(mode)) matchesMode = r.getNotes() != null && !r.getNotes().trim().isEmpty();

            return matchesText && matchesMode;
        });

        refreshCount();
    }

    private void refreshCount() {
        countLabel.setText("Показано: " + table.getItems().size() + " / Всего: " + appState.getRecords().size());
    }

    private void openRecordDialog(HealthRecord recordToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthtracker/view/RecordDialog.fxml"));
            Parent root = loader.load();

            RecordDialogController controller = loader.getController();
            controller.setAppState(appState);
            controller.setEditTarget(recordToEdit);

            Stage stage = new Stage();
            stage.setTitle(recordToEdit == null ? "Добавить запись" : "Редактировать запись");
            stage.initModality(Modality.APPLICATION_MODAL);

            Stage owner = (Stage) table.getScene().getWindow();
            stage.initOwner(owner);

            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            error("Не удалось открыть окно", e.getMessage());
        }
    }

    private Optional<ButtonType> confirm(String title, String header, String content) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        return a.showAndWait();
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