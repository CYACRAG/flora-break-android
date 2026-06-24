package com.florabreak.app.ui;

public class UiStressState {

    private final double stressScore;
    private final String stressLabel;
    private final String description;
    private final int heartRate;
    private final String recommendation;

    public UiStressState(
            double stressScore,
            String stressLabel,
            String description,
            int heartRate,
            String recommendation
    ) {
        this.stressScore = stressScore;
        this.stressLabel = stressLabel;
        this.description = description;
        this.heartRate = heartRate;
        this.recommendation = recommendation;
    }

    public double getStressScore() {
        return stressScore;
    }

    public String getStressLabel() {
        return stressLabel;
    }

    public String getDescription() {
        return description;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public String getRecommendation() {
        return recommendation;
    }
}