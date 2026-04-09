package com.example.megdeal_earning.Models;

public class Transaction {
    private String id;
    private String userId;
    private String type;
    private String description;
    private double amount;
    private String status;
    private long timeStamp;
    private String referenceId;
    public Transaction(){}
    public Transaction(String userId, String type, String description, double amount){
        this.userId = userId;
        this.type=type;
        this.description=description;
        this.amount=amount;
        this.status="pending";
        this.timeStamp=System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getUserId(){return userId;}
    public void setUserId(String userId){this.userId=userId;}

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public double getAmount(){return amount;}
    public void setAmount(double amount){this.amount=amount;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public long getTimeStamp(){return timeStamp;}
    public void setTimeStamp(long timeStamp){this.timeStamp=timeStamp;}

    public String getReferenceId(){return referenceId;}
    public void setReferenceId(String referenceId){this.referenceId=referenceId;}
}
