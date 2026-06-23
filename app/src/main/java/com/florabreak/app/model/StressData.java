package com.florabreak.app.model;

public class StressData {

    private double currentHrv;
    private double normalHrv;
    private int heartRate;
    private int systolicBloodPressure;
    private int diastolicBloodPressure;
    private long timestamp;

    public StressData(double currentHrv, double normalHrv, int heartRate,
                      int systolicBloodPressure, int diastolicBloodPressure,
                      long timestamp) {
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

    public long getTimestamp() {
        return timestamp;
    }
}
