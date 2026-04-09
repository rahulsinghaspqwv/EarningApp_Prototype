package com.example.megdeal_earning.Models;

public class User {
    private String id;
    private String name;
    private String mobile;
    private String email;
    private String paytmNumber;
    private double balance;
    private double totalEarned;
    private double totalWithdrawn;
    private String referralCode;
    private String referredBy;
    private boolean isActive;
    // Constructor
    public User() {}
    public User(String name, String mobile, String paytmNumber){
        this.name = name;
        this.mobile = mobile;
        this.paytmNumber = paytmNumber;
        this.balance = 0.0;
        this.totalEarned = 0.0;
        this.totalWithdrawn = 0.0;
        this.isActive = true;
    }

    // Getter and Setters
    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getMobile(){return mobile;}
    public void setMobile(String mobile){this.mobile=mobile;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}

    public String getPaytmNumber(){return paytmNumber;}
    public void setPaytmNumber(String paytmNumber){this.paytmNumber=paytmNumber;}

    public double getBalance(){return balance;}
    public void setBalance(double balance){this.balance=balance;}

    public double getTotalEarned(){return totalEarned;}
    public void setTotalEarned(double totalEarned){this.totalEarned=totalEarned;}

    public double getTotalWithdrawn(){return totalWithdrawn;}
    public void setTotalWithdrawn(double totalWithdrawn){this.totalWithdrawn=totalWithdrawn;}

    public String getReferralCode(){return referralCode;}
    public void setReferralCode(String referralCode){this.referralCode=referralCode;}

    public String getReferredBy(){return referredBy;}
    public void setReferredBy(String referredBy){this.referredBy=referredBy;}

    public boolean isActive(){return isActive;}
    public void setActive(boolean active){isActive=active;}

}
