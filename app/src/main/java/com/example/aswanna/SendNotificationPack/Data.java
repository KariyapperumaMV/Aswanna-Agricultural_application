package com.example.aswanna.SendNotificationPack;

public class Data {

    public String projectName;
    public String projectType;

    public  String imageUrl;

    public  String location;

    public String price;


    public Data(String projectName, String projectType, String imageUrl, String location, String price) {
        this.projectName = projectName;
        this.projectType = projectType;
        this.imageUrl = imageUrl;
        this.location = location;
        this.price = price;
    }

    public Data() {
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }
}
