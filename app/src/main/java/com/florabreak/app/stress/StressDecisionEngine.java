package com.florabreak.app.stress;

import com.florabreak.app.model.StressData;
import com.florabreak.app.model.StressResult;

public class StressDecisionEngine {

    private final StressScoreCalculator calculator;

    public StressDecisionEngine() {
        this.calculator = new StressScoreCalculator();
    }

    public StressResult analyzeStress(StressData data) {
        int score = calculator.calculateScore(data);

        String label = getLabelForScore(score);
        boolean breakRecommended = score >= 4;
        String explanation = getExplanationForScore(score);

        return new StressResult(score, label, breakRecommended, explanation);
    }

    private String getLabelForScore(int score) {
        if (score <= 1) {
            return "Normal";
        } else if (score <= 3) {
            return "Leicht erhöht";
        } else if (score <= 5) {
            return "Erhöhter Stress";
        } else {
            return "Hoher Stress";
        }
    }

    private String getExplanationForScore(int score) {
        if (score <= 1) {
            return "Deine Werte wirken unauffällig. Aktuell ist keine Pause notwendig.";
        } else if (score <= 3) {
            return "Deine Werte sind leicht erhöht. Eine kurze Unterbrechung kann sinnvoll sein.";
        } else if (score <= 5) {
            return "Deine Werte zeigen eine mögliche Belastung. Eine kurze Pause wird empfohlen.";
        } else {
            return "Deine Werte zeigen eine hohe Belastung. Eine Pause wird deutlich empfohlen.";
        }
    }
}
