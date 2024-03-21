package com.example.aswanna.Model;

public class RatingFeedback {
    private double rating;
    private String feedback;
    private String investorID;

    public double getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getInvestorID() {
        return investorID;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setInvestorID(String investorID) {
        this.investorID = investorID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public String getFarmerID() {
        return farmerID;
    }

    public RatingFeedback(double rating, String feedback, String investorID, String farmerID) {
        this.rating = rating;
        this.feedback = feedback;
        this.investorID = investorID;
        this.farmerID = farmerID;
    }

    public RatingFeedback() {
    }

    private String farmerID;

    // Constructors, getters, setters
}
