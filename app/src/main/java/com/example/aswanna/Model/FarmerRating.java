package com.example.aswanna.Model;

public class FarmerRating {
    private String farmerID;
    private double rating;
    private int totalRating;

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public String getFarmerID() {
        return farmerID;
    }

    public double getRating() {
        return rating;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public FarmerRating() {
    }

    public FarmerRating(String farmerID, double rating, int totalRating) {
        this.farmerID = farmerID;
        this.rating = rating;
        this.totalRating = totalRating;
    }
}
