package com.florabreak.app.stress;

import com.florabreak.app.model.StressData;
import com.florabreak.app.model.StressResult;

/**
 * Bewertet Gesundheitsdaten und erzeugt ein StressResult.
 *
 * Die Engine entscheidet zentral:
 * - HRV-Score
 * - Herzfrequenz-Score
 * - Gesamt-Score
 * - Label
 * - Pausenempfehlung
 * - Erklärung
 */
public class StressDecisionEngine {

    private final StressScoreCalculator calculator;

    public StressDecisionEngine() {
        this.calculator = new StressScoreCalculator();
    }

    public StressResult analyzeStress(StressData data) {
        int hrvScore = calculator.calculateHrvScore(data);
        int heartRateScore = calculator.calculateHeartRateScore(data);
        int totalScore = calculator.calculateTotalScore(data);

        String label = getLabelForScore(totalScore);
        boolean breakRecommended = totalScore >= 6;
        String explanation = getExplanationForScore(totalScore, hrvScore, heartRateScore);

        return new StressResult(
                totalScore,
                hrvScore,
                heartRateScore,
                label,
                breakRecommended,
                explanation
        );
    }

    private String getLabelForScore(int score) {
        if (score <= 2) {
            return "Normal";
        } else if (score <= 4) {
            return "Leicht erhöht";
        } else if (score <= 7) {
            return "Erhöhter Stress";
        } else {
            return "Hoher Stress";
        }
    }

    private String getExplanationForScore(int score, int hrvScore, int heartRateScore) {
        String baseText;

        if (score <= 2) {
            baseText = "Deine Werte wirken unauffällig. Aktuell ist keine Pause notwendig.";
        } else if (score <= 4) {
            baseText = "Deine Werte sind leicht erhöht. Eine kurze Unterbrechung kann sinnvoll sein.";
        } else if (score <= 7) {
            baseText = "Deine Werte zeigen eine mögliche Belastung. Eine kurze Pause wird empfohlen.";
        } else {
            baseText = "Deine Werte zeigen eine hohe Belastung. Eine Pause wird deutlich empfohlen.";
        }

        return baseText
                + " HRV-Anteil: "
                + hrvScore
                + "/6, Puls-Anteil: "
                + heartRateScore
                + "/4.";
    }
}
