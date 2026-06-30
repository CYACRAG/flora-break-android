package com.florabreak.app.model;

/**
 * Ergebnis der Stressbewertung.
 *
 * score ist der finale Gesamtwert von 0 bis 10.
 * Zusätzlich werden HRV- und Herzfrequenz-Anteil gespeichert,
 * damit die App später transparenter erklären kann, wodurch der Score entsteht.
 */
public class StressResult {

    private int score;
    private int hrvScore;
    private int heartRateScore;

    private String label;
    private boolean breakRecommended;
    private String explanation;

    /**
     * Alter Konstruktor bleibt erhalten, damit bestehender Code nicht bricht.
     */
    public StressResult(int score, String label, boolean breakRecommended, String explanation) {
        this(score, 0, 0, label, breakRecommended, explanation);
    }

    /**
     * Neuer Konstruktor mit Einzelwerten.
     */
    public StressResult(
            int score,
            int hrvScore,
            int heartRateScore,
            String label,
            boolean breakRecommended,
            String explanation
    ) {
        this.score = score;
        this.hrvScore = hrvScore;
        this.heartRateScore = heartRateScore;
        this.label = label;
        this.breakRecommended = breakRecommended;
        this.explanation = explanation;
    }

    /**
     * Finaler Gesamt-Score von 0 bis 10.
     */
    public int getScore() {
        return score;
    }

    /**
     * HRV-Anteil von 0 bis 6.
     */
    public int getHrvScore() {
        return hrvScore;
    }

    /**
     * Herzfrequenz-Anteil von 0 bis 4.
     */
    public int getHeartRateScore() {
        return heartRateScore;
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
