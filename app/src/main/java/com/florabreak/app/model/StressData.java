package com.florabreak.app.model;

/**
 * Diese Klasse speichert Gesundheitsdaten,
 * die später für die Stress-Berechnung genutzt werden.
 *
 * Für den Prototyp kommen diese Werte erstmal aus Mock-Daten.
 */
public class StressData {

    private double currentHrv;
    private double normalHrv;
    private int heartRate;
    private int systolicBloodPressure;
    private int diastolicBloodPressure;
    private long timestamp;

    public StressData(
            double currentHrv,
            double normalHrv,
            int heartRate,
            int systolicBloodPressure,
            int diastolicBloodPressure,
            long timestamp
    ) {
        this.currentHrv = currentHrv;
        this.normalHrv = normalHrv;
        this.heartRate = heartRate;
        this.systolicBloodPressure = systolicBloodPressure;
        this.diastolicBloodPressure = diastolicBloodPressure;
        this.timestamp = timestamp;
    }

    public double getCurrentHrv() {
        return currentHrv;
    }

    public void setCurrentHrv(double currentHrv) {
        this.currentHrv = currentHrv;
    }

    public double getNormalHrv() {
        return normalHrv;
    }

    public void setNormalHrv(double normalHrv) {
        this.normalHrv = normalHrv;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(int systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public int getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(int diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}