package com.florabreak.app.model;

/**
 * Zentrales Profilmodell für Flora Break.
 *
 * Diese Daten werden beim ersten App-Start vom Nutzer ausgefüllt
 * und später über ein Repository gespeichert.
 *
 * TODO: In einer finalen Version können diese Daten in Room/Firebase
 *       statt nur lokal gespeichert werden.
 */
public class UserProfile {

    private final String name;
    private final int age;
    private final int heightCm;
    private final int weightKg;
    private final String workStartTime;
    private final String workEndTime;
    private final int subjectiveStressLevel;
    private final boolean profileCompleted;

    public UserProfile(String name,
                       int age,
                       int heightCm,
                       int weightKg,
                       String workStartTime,
                       String workEndTime,
                       int subjectiveStressLevel,
                       boolean profileCompleted) {
        this.name = name;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.subjectiveStressLevel = subjectiveStressLevel;
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

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}
