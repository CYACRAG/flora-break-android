package com.florabreak.app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Datenbankzugriff für gespeicherte Pausen.
 */
@Dao
public interface BreakDao {

    @Insert
    long insertBreak(BreakEntity breakEntity);

    @Update
    void updateBreak(BreakEntity breakEntity);

    @Query("SELECT * FROM breaks WHERE id = :breakId LIMIT 1")
    BreakEntity getBreakById(long breakId);

    @Query("UPDATE breaks SET endedAt = :endedAt, durationMinutes = :durationMinutes, rating = :rating, feedbackText = :feedbackText WHERE id = :breakId")
    void finishBreak(
            long breakId,
            long endedAt,
            int durationMinutes,
            int rating,
            String feedbackText
    );

    @Query("UPDATE breaks SET photoProofPath = :photoProofPath, photoProofTaken = 1 WHERE id = :breakId")
    void updatePhotoProof(long breakId, String photoProofPath);

    @Query("SELECT * FROM breaks ORDER BY startedAt DESC")
    List<BreakEntity> getAllBreaks();

    @Query("SELECT * FROM breaks ORDER BY startedAt DESC LIMIT 1")
    BreakEntity getLatestBreak();

    @Query("SELECT * FROM breaks WHERE startedAt >= :fromTimestamp ORDER BY startedAt DESC")
    List<BreakEntity> getBreaksSince(long fromTimestamp);

    @Query("SELECT COUNT(*) FROM breaks WHERE startedAt >= :fromTimestamp")
    int countBreaksSince(long fromTimestamp);

    @Query("SELECT AVG(stressScore) FROM breaks WHERE startedAt >= :fromTimestamp")
    Double getAverageStressSince(long fromTimestamp);

    @Query("SELECT AVG(rating) FROM breaks WHERE startedAt >= :fromTimestamp AND rating > 0")
    Double getAverageRatingSince(long fromTimestamp);

    @Query("SELECT COUNT(*) FROM breaks WHERE startedAt >= :fromTimestamp AND photoProofTaken = 1")
    int countPhotoProofsSince(long fromTimestamp);

    @Query("SELECT SUM(durationMinutes) FROM breaks WHERE startedAt >= :fromTimestamp")
    Integer getTotalBreakMinutesSince(long fromTimestamp);
}
