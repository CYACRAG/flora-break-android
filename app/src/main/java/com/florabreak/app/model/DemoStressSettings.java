package com.florabreak.app.model;

/**
 * Einstellungen für den Demo-Modus.
 *
 * Diese Werte können später über Slider im Profil-Screen gesetzt werden.
 * Sie simulieren Smartwatch-/Health-Connect-Daten für die StressEngine.
 */
public class DemoStressSettings {

    private final boolean demoModeEnabled;
    private final double currentHrv;
    private final double normalHrv;
    private final int heartRate;
    private final int systolicBloodPressure;
    private final int diastolicBloodPressure;

    public DemoStressSettings(boolean demoModeEnabled,
                              double currentHrv,
                              double normalHrv,
                              int heartRate,
                              int systolicBloodPressure,
                              int diastolicBloodPressure) {
        this.demoModeEnabled = demoModeEnabled;
        this.currentHrv = currentHrv;
        this.normalHrv = normalHrv;
        this.heartRate = heartRate;
        this.systolicBloodPressure = systolicBloodPressure;
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public boolean isDemoModeEnabled() {
        return demoModeEnabled;
    }

    public double getCurrentHrv() {
        return currentHrv;
    }

    public double getNormalHrv() {
        return normalHrv;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public int getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }
}
