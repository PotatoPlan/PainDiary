package com.example.paindiaryapp;

public class Records {
    private String date;
    private String painLocation;
    private String moodLevel;
    private String stepsTaken;
    private String painIntensity;

    public Records(String date, String painLocation, String moodLevel, String stepsTaken, String painIntensity) {
        this.date = date;
        this.painLocation = painLocation;
        this.moodLevel = moodLevel;
        this.stepsTaken = stepsTaken;
        this.painIntensity = painIntensity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPainLocation() {
        return painLocation;
    }

    public void setPainLocation(String painLocation) {
        this.painLocation = painLocation;
    }

    public String getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(String moodLevel) {
        this.moodLevel = moodLevel;
    }

    public String getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(String stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public String getPainIntensity() {
        return painIntensity;
    }

    public void setPainIntensity(String painIntensity) {
        this.painIntensity = painIntensity;
    }
}
