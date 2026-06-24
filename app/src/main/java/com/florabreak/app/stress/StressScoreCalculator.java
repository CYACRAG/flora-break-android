package com.florabreak.app.stress;

import com.florabreak.app.model.StressData;

public class StressScoreCalculator {

    public int calculateScore(StressData data) {
        if (data == null) {
            return 0;
        }

        double normalHrv = data.getNormalHrv();
        double currentHrv = data.getCurrentHrv();

        if (normalHrv <= 0) {
            return 0;
        }

        double ratio = currentHrv / normalHrv;

        if (ratio <= 1.0) {
            return 0;
        }

        if (ratio >= 1.3) {
            return 6;
        }

        int score = (int) Math.round(((ratio - 1.0) / 0.3) * 6);

        if (score < 0) {
            return 0;
        }

        if (score > 6) {
            return 6;
        }

        return score;
    }
}
