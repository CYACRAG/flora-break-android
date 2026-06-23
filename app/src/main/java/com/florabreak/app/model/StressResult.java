package com.florabreak.app.model;

public class StressResult {

    private int score;
    private String label;
    private boolean breakRecommended;
    private String explanation;

    public StressResult(int score, String label, boolean breakRecommended, String explanation) {
        this.score = score;
        this.label = label;
        this.breakRecommended = breakRecommended;
        this.explanation = explanation;
    }

    public int getScore() {
        return score;
    }

    public String getLabel() {
        return label;
    }

    public boolean isBreakRecommended() {
        return breakRecommended;
    }

    public String getExplanation() {
        return explanation;
    }
}
