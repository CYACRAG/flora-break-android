package com.florabreak.app.data.repository;

import android.content.Context;

import com.florabreak.app.data.local.BreakDao;
import com.florabreak.app.data.local.BreakEntity;
import com.florabreak.app.data.local.FloraBreakDatabase;

import java.util.List;

/**
 * Repository für echte gespeicherte Pausen.
 *
 * Zentrale Schnittstelle für:
 * - ActiveBreakActivity
 * - Feedback
 * - History
 * - Statistik
 * - späteres HR-Dashboard
 */
public class BreakSessionRepository {

    private final BreakDao breakDao;

    public BreakSessionRepository(Context context) {
        FloraBreakDatabase database = FloraBreakDatabase.getInstance(context);
        this.breakDao = database.breakDao();
    }

    public long startBreak(
            String routeName,
            String routeType,
            double routeLatitude,
            double routeLongitude,
            int plannedDurationMinutes,
            int stressScore,
            String stressLabel
    ) {
        long now = System.currentTimeMillis();

        BreakEntity breakEntity = new BreakEntity(
                now,
                0L,
                plannedDurationMinutes,
                routeName,
                routeType,
                routeLatitude,
                routeLongitude,
                stressScore,
                stressLabel,
                0,
                "",
                "",
                false,
                now
        );

        return breakDao.insertBreak(breakEntity);
    }

    public void finishBreak(
            long breakId,
            int durationMinutes,
            int rating,
            String feedbackText
    ) {
        breakDao.finishBreak(
                breakId,
                System.currentTimeMillis(),
                durationMinutes,
                rating,
                feedbackText
        );
    }

    public void savePhotoProof(long breakId, String photoProofPath) {
        breakDao.updatePhotoProof(breakId, photoProofPath);
    }

    public BreakEntity getBreakById(long breakId) {
        return breakDao.getBreakById(breakId);
    }

    public long saveBreak(BreakEntity breakEntity) {
        return breakDao.insertBreak(breakEntity);
    }

    public void updateBreak(BreakEntity breakEntity) {
        breakDao.updateBreak(breakEntity);
    }

    public List<BreakEntity> getAllBreaks() {
        return breakDao.getAllBreaks();
    }

    public BreakEntity getLatestBreak() {
        return breakDao.getLatestBreak();
    }

    public List<BreakEntity> getBreaksLastSevenDays() {
        return breakDao.getBreaksSince(getSevenDaysAgoTimestamp());
    }

    public int getBreakCountLastSevenDays() {
        return breakDao.countBreaksSince(getSevenDaysAgoTimestamp());
    }

    public double getAverageStressLastSevenDays() {
        Double value = breakDao.getAverageStressSince(getSevenDaysAgoTimestamp());
        if (value == null) {
            return 0.0;
        }
        return value;
    }

    public double getAverageRatingLastSevenDays() {
        Double value = breakDao.getAverageRatingSince(getSevenDaysAgoTimestamp());
        if (value == null) {
            return 0.0;
        }
        return value;
    }

    public int getPhotoProofCountLastSevenDays() {
        return breakDao.countPhotoProofsSince(getSevenDaysAgoTimestamp());
    }

    public int getTotalBreakMinutesLastSevenDays() {
        Integer value = breakDao.getTotalBreakMinutesSince(getSevenDaysAgoTimestamp());
        if (value == null) {
            return 0;
        }
        return value;
    }

    private long getSevenDaysAgoTimestamp() {
        return System.currentTimeMillis() - 7L * 24L * 60L * 60L * 1000L;
	    }
	public List<BreakEntity> getBreaksLastMonth() {
	    return breakDao.getBreaksSince(getOneMonthAgoTimestamp());
	}

	private long getOneMonthAgoTimestamp() {
	    return System.currentTimeMillis() - 30L * 24L * 60L * 60L * 1000L;
	}
}
