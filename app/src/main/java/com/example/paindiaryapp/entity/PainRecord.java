package com.example.paindiaryapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PainRecord {
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "Date_of_Entry")
    public String dateOfEntry;

    @ColumnInfo (name = "Pain_Intensity_Level")
    public String painIntensity;

    @ColumnInfo (name = "Pain_Location")
    public String painLocation;

    @ColumnInfo (name = "Mood_Level")
    public String moodLevel;

    @ColumnInfo (name = "Steps_Taken")
    public String stepsTaken;

    @ColumnInfo (name = "Goal_Steps")
    public String goalSteps;

    @ColumnInfo (name = "Temperature")
    public String temperature;

    @ColumnInfo (name = "Humidity")
    public String humidity;

    @ColumnInfo (name = "Pressure")
    public String pressure;

    @ColumnInfo (name = "User_Email")
    public String userEmail;



    public PainRecord(String dateOfEntry, String painIntensity, String painLocation, String moodLevel, String stepsTaken, String goalSteps, String temperature, String humidity, String pressure, String userEmail) {
        this.dateOfEntry = dateOfEntry;
        this.painIntensity = painIntensity;
        this.painLocation = painLocation;
        this.moodLevel = moodLevel;
        this.stepsTaken = stepsTaken;
        this.goalSteps = goalSteps;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.userEmail = userEmail;
    }
}
