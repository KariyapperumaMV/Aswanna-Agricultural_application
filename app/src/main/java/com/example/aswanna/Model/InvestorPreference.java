package com.example.aswanna.Model;

public class InvestorPreference {

    private String fcmToken;
    private String projectLocation;
    private String projectType;

    // Constructors, getters, and setters
    public InvestorPreference() { }

    public InvestorPreference(String fcmToken, String projectLocation, String projectType) {
        this.fcmToken = fcmToken;
        this.projectLocation = projectLocation;
        this.projectType = projectType;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }


}
