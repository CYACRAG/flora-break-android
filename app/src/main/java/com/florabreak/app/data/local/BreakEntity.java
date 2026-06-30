package com.florabreak.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Datenbank-Eintrag für eine abgeschlossene oder laufende Flora-Break-Pause.
 *
 * Wird lokal gespeichert und später für Statistik, History und HR-Dashboard genutzt.
 */
@Entity(tableName = "breaks")
public class BreakEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long startedAt;
    public long endedAt;
    public int durationMinutes;

    public String routeName;
    public String routeType;
    public double routeLatitude;
    public double routeLongitude;

    public int stressScore;
    public String stressLabel;

    public int rating;
    public String feedbackText;

    public String photoProofPath;
    public boolean photoProofTaken;

    public long createdAt;

    public BreakEntity(
            long startedAt,
            long endedAt,
            int durationMinutes,
            String routeName,
            String routeType,
            double routeLatitude,
            double routeLongitude,
            int stressScore,
            String stressLabel,
            int rating,
            String feedbackText,
            String photoProofPath,
            boolean photoProofTaken,
            long createdAt
    ) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationMinutes = durationMinutes;
        this.routeName = routeName;
        this.routeType = routeType;
        this.routeLatitude = routeLatitude;
        this.routeLongitude = routeLongitude;
        this.stressScore = stressScore;
        this.stressLabel = stressLabel;
        this.rating = rating;
        this.feedbackText = feedbackText;
        this.photoProofPath = photoProofPath;
        this.photoProofTaken = photoProofTaken;
        this.createdAt = createdAt;
    }
}
