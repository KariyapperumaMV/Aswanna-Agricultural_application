package com.example.aswanna.Model;

public class Inquiry {
    private String farmerName;
    private String farmerID;
    private String postedDate;
    private String Image;
    private String projectName;
    private String projectId;
    private String DocumentID;
    private String InvestorId;
    private String Status;

    public String getFarmerID() {
        return farmerID;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public void setInvestorId(String investorId) {
        InvestorId = investorId;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getImage() {
        return Image;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public String getInvestorId() {
        return InvestorId;
    }

    public String getStatus() {
        return Status;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public Inquiry() {
    }

    public Inquiry(String farmerName, String farmerID, String postedDate, String image, String projectName, String projectId, String documentID, String investorId, String status) {
        this.farmerName = farmerName;
        this.farmerID = farmerID;
        this.postedDate = postedDate;
        Image = image;
        this.projectName = projectName;
        this.projectId = projectId;
        DocumentID = documentID;
        InvestorId = investorId;
        Status = status;
    }




// Additional fields you want to store

    // Constructors, getters, and setters
}
