package com.florabreak.app.stress;

import com.florabreak.app.model.StressData;

/**
 * Berechnet den Stress-Score aus Gesundheitsdaten.
 *
 * Fachliche Logik:
 * - HRV sinkt im Vergleich zur persönlichen Baseline -> Stress steigt
 * - Puls steigt -> Zusatzstress steigt
 *
 * Score-Aufteilung:
 * - HRV-Score: 0 bis 6
 * - Herzfrequenz-Score: 0 bis 4
 * - Gesamt-Score: 0 bis 10
 */
public class StressScoreCalculator {

    public int calculateScore(StressData data) {
        return calculateTotalScore(data);
    }

    /**
     * HRV-Stressscore von 0 bis 6.
     *
     * Wichtig:
     * Höhere HRV spricht eher für Erholung.
     * Niedrigere HRV im Vergleich zur normalen HRV spricht eher für Belastung.
     *
     * Formel:
     * normale HRV / aktuelle HRV
     *
     * Verhältnis 1.00 -> Score 0
     * Verhältnis 1.10 -> ca. Score 2
     * Verhältnis 1.20 -> ca. Score 4
     * Verhältnis 1.30+ -> Score 6
     */
    public int calculateHrvScore(StressData data) {
        if (data == null) {
            return 0;
        }

        double normalHrv = data.getNormalHrv();
        double currentHrv = data.getCurrentHrv();

        if (normalHrv <= 0 || currentHrv <= 0) {
            return 0;
        }

        double ratio = normalHrv / currentHrv;

        if (ratio <= 1.0) {
            return 0;
        }

        if (ratio >= 1.3) {
            return 6;
        }

        int score = (int) Math.round(((ratio - 1.0) / 0.3) * 6);

        return clamp(score, 0, 6);
    }

    /**
     * Herzfrequenz-Stressscore von 0 bis 4.
     *
     * Prototyp-Logik:
     * <= 80 bpm  -> 0
     * <= 90 bpm  -> 1
     * <= 100 bpm -> 2
     * <= 110 bpm -> 3
     * > 110 bpm  -> 4
     */
    public int calculateHeartRateScore(StressData data) {
        if (data == null) {
            return 0;
        }

        int heartRate = data.getHeartRate();

        if (heartRate <= 0) {
            return 0;
        }

        if (heartRate <= 80) {
            return 0;
        } else if (heartRate <= 90) {
            return 1;
        } else if (heartRate <= 100) {
            return 2;
        } else if (heartRate <= 110) {
            return 3;
        } else {
            return 4;
        }
    }

    public int calculateTotalScore(StressData data) {
        int hrvScore = calculateHrvScore(data);
        int heartRateScore = calculateHeartRateScore(data);

        return clamp(hrvScore + heartRateScore, 0, 10);
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }
}

