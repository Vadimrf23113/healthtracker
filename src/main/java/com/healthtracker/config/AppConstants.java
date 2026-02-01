package com.healthtracker.config;

public final class AppConstants {

    private AppConstants() {
    }

    public static final String APP_NAME = "HealthTracker";
    public static final String APP_VERSION = "1.0.0";

    public static final String ROOT_VIEW = "/com/healthtracker/view/RootView.fxml";

    public static final String DASHBOARD_VIEW = "/com/healthtracker/view/DashboardView.fxml";
    public static final String RECORDS_VIEW   = "/com/healthtracker/view/RecordsView.fxml";
    public static final String STATS_VIEW     = "/com/healthtracker/view/StatsView.fxml";

    public static final String RECORD_DIALOG_VIEW  = "/com/healthtracker/view/RecordDialog.fxml";
    public static final String PROFILE_DIALOG_VIEW = "/com/healthtracker/view/ProfileDialog.fxml";
    public static final String ABOUT_DIALOG_VIEW   = "/com/healthtracker/view/AboutDialog.fxml";

    public static final String MAIN_CSS = "/com/healthtracker/css/app.css";

    public static final String DATA_FILE_NAME = "healthtracker.json";


    public static final int MIN_WEIGHT_KG = 1;
    public static final int MAX_WEIGHT_KG = 400;

    public static final int MIN_WATER_ML = 0;
    public static final int MAX_WATER_ML = 20_000;

    public static final double MIN_SLEEP_HOURS = 0.0;
    public static final double MAX_SLEEP_HOURS = 24.0;

    public static final int MIN_AGE = 1;
    public static final int MAX_AGE = 120;

    public static final int MIN_HEIGHT_CM = 50;
    public static final int MAX_HEIGHT_CM = 250;
}
