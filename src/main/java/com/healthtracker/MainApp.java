package com.healthtracker;

import com.healthtracker.config.AppConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstants.ROOT_VIEW));
        Scene scene = new Scene(loader.load());

        String css = getClass().getResource(AppConstants.MAIN_CSS).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle(AppConstants.APP_NAME);
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}