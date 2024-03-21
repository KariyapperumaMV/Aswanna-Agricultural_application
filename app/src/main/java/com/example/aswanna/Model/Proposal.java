package com.example.aswanna.Model;


import java.io.Serializable;

public class Proposal implements Serializable {

    private String pid;
    private String investorID;
    private String startDate;
    private String endDate;

    private String documentID;
    private String farmerID;

    private String projectName;
    private String projectType;
    private String projectLocation;
    private String projectDurationInMonths;
    private String projectDescription;
    private int fundingRequired;
    private String expectedReturnsOnInvestment;
    private String imageOneLink;
    private String imageTwoLink;

    private String farmerName;
    private String farmerProfileImage;
    private String farmerLevel;
    private String Status;

    private String postedDate;

    // Constructors
    public Proposal() {

    }

    public Proposal(String investorID,String startDate,String endDate,String farmerLevel,String farmerProfileImage,String farmerName,String PID, String documentID, String farmerID, String projectName, String projectType, String projectLocation, String projectDurationInMonths, String projectDescription, int fundingRequired, String expectedReturnsOnInvestment, String imageOneLink, String imageTwoLink, String status,String postedDate) {
        this.pid = PID;
        this.documentID = documentID;
        this.farmerID = farmerID;
        this.projectName = projectName;
        this.projectType = projectType;
        this.projectLocation = projectLocation;
        this.projectDurationInMonths = projectDurationInMonths;
        this.projectDescription = projectDescription;
        this.fundingRequired = fundingRequired;
        this.expectedReturnsOnInvestment = expectedReturnsOnInvestment;
        this.imageOneLink = imageOneLink;
        this.imageTwoLink = imageTwoLink;
        this.farmerProfileImage=farmerProfileImage;
        this.farmerName=farmerName;
        this.farmerLevel=farmerLevel;
        this.investorID=investorID;
        this.startDate=startDate;
        this.endDate=endDate;
        Status = status;
        this.postedDate = postedDate;
    }

    public void setInvestorID(String investorID) {
        this.investorID = investorID;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getInvestorID() {
        return investorID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setFarmerLevel(String farmerLevel) {
        this.farmerLevel = farmerLevel;
    }

    public String getFarmerLevel() {
        return farmerLevel;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public void setFarmerProfileImage(String farmerProfileImage) {
        this.farmerProfileImage = farmerProfileImage;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getFarmerProfileImage() {
        return farmerProfileImage;
    }

    public String getPID() {
        return pid;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getFarmerID() {
        return farmerID;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public String getProjectDurationInMonths() {
        return projectDurationInMonths;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public int getFundingRequired() {
        return fundingRequired;
    }

    public String getExpectedReturnsOnInvestment() {
        return expectedReturnsOnInvestment;
    }

    public String getImageOneLink() {
        return imageOneLink;
    }

    public void setPID(String PID) {
        this.pid = PID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public void setProjectDurationInMonths(String projectDurationInMonths) {
        this.projectDurationInMonths = projectDurationInMonths;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setFundingRequired(int fundingRequired) {
        this.fundingRequired = fundingRequired;
    }

    public void setExpectedReturnsOnInvestment(String expectedReturnsOnInvestment) {
        this.expectedReturnsOnInvestment = expectedReturnsOnInvestment;
    }

    public void setImageOneLink(String imageOneLink) {
        this.imageOneLink = imageOneLink;
    }

    public void setImageTwoLink(String imageTwoLink) {
        this.imageTwoLink = imageTwoLink;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImageTwoLink() {
        return imageTwoLink;
    }

    public String getStatus() {
        return Status;
    }

    public String getPostedDate() {
        return postedDate;
    }

    // Getters and setters

}
