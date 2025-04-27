package com.example.cognitivekidshometraining;

public class ReportWeeklyReport {
    public String date;
    public String childName;
    public String contact;
    public int age;
    public String disorder;
    public String doctorName;
    public int therapiesCompleted;
    public int therapiesMissed;
    public int activitiesCompleted;
    public int activitiesMissed;
    public String testsAndResults;
    public String clinicalObservation;
    public int performanceScore;
    public int progressScore;
    public String doctorNote;

    // Constructor
    public ReportWeeklyReport(String date, String childName, String contact, int age, String disorder, String doctorName,
                              int therapiesCompleted, int therapiesMissed,
                              int activitiesCompleted, int activitiesMissed,
                              String testsAndResults, String clinicalObservation,
                              int performanceScore, int progressScore, String doctorNote) {
        this.date = date;
        this.childName = childName;
        this.contact = contact;
        this.age = age;
        this.disorder = disorder;
        this.doctorName = doctorName;
        this.therapiesCompleted = therapiesCompleted;
        this.therapiesMissed = therapiesMissed;
        this.activitiesCompleted = activitiesCompleted;
        this.activitiesMissed = activitiesMissed;
        this.testsAndResults = testsAndResults;
        this.clinicalObservation = clinicalObservation;
        this.performanceScore = performanceScore;
        this.progressScore = progressScore;
        this.doctorNote = doctorNote;
    }

    // Getters (optional but good practice to have)
    public String getDate() {
        return date;
    }

    public String getChildName() {
        return childName;
    }

    public String getContact() {
        return contact;
    }

    public int getAge() {
        return age;
    }

    public String getDisorder() {
        return disorder;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getTherapiesCompleted() {
        return therapiesCompleted;
    }

    public int getTherapiesMissed() {
        return therapiesMissed;
    }

    public int getActivitiesCompleted() {
        return activitiesCompleted;
    }

    public int getActivitiesMissed() {
        return activitiesMissed;
    }

    public String getTestsAndResults() {
        return testsAndResults;
    }

    public String getClinicalObservation() {
        return clinicalObservation;
    }

    public int getPerformanceScore() {
        return performanceScore;
    }

    public int getProgressScore() {
        return progressScore;
    }

    public String getDoctorNote() {
        return doctorNote;
    }
}
