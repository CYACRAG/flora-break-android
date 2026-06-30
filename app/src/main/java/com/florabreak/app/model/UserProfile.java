package com.florabreak.app.model;

/**
 * Zentrales Profilmodell für Flora Break.
 *
 * Diese Daten werden beim ersten App-Start vom Nutzer ausgefüllt
 * und lokal gespeichert.
 *
 * Der Arbeitsort kann später für Maps verwendet werden:
 * Wenn ein Arbeitsort gespeichert ist, werden Pausenrouten bevorzugt
 * ab diesem Standort berechnet.
 */
public class UserProfile {

    private final String name;
    private final int age;
    private final int heightCm;
    private final int weightKg;
    private final String workStartTime;
    private final String workEndTime;
    private final int subjectiveStressLevel;

    private final String workLocationName;
    private final double workLatitude;
    private final double workLongitude;
    private final boolean workLocationSaved;

    private final boolean profileCompleted;

    /**
     * Alter Konstruktor bleibt erhalten, damit bestehender Code nicht bricht.
     */
    public UserProfile(String name,
                       int age,
                       int heightCm,
                       int weightKg,
                       String workStartTime,
                       String workEndTime,
                       int subjectiveStressLevel,
                       boolean profileCompleted) {
        this(
                name,
                age,
                heightCm,
                weightKg,
                workStartTime,
                workEndTime,
                subjectiveStressLevel,
                "",
                0.0,
                0.0,
                false,
                profileCompleted
        );
    }

    public UserProfile(String name,
                       int age,
                       int heightCm,
                       int weightKg,
                       String workStartTime,
                       String workEndTime,
                       int subjectiveStressLevel,
                       String workLocationName,
                       double workLatitude,
                       double workLongitude,
                       boolean workLocationSaved,
                       boolean profileCompleted) {
        this.name = name;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.subjectiveStressLevel = subjectiveStressLevel;
        this.workLocationName = workLocationName;
        this.workLatitude = workLatitude;
        this.workLongitude = workLongitude;
        this.workLocationSaved = workLocationSaved;
        this.profileCompleted = profileCompleted;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public int getSubjectiveStressLevel() {
        return subjectiveStressLevel;
    }

    public String getWorkLocationName() {
        return workLocationName;
    }

    public double getWorkLatitude() {
        return workLatitude;
    }

    public double getWorkLongitude() {
        return workLongitude;
    }

    public boolean isWorkLocationSaved() {
        return workLocationSaved;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}
