package com.example.megdeal_earning.Models;

public class Offer {
    private String id;
    private String title;
    private String description;
    private double reward;
    private String imageUrl;
    private String actionUrl;
    private boolean isActive;
    private Offer(){}
    public Offer(String id, String title, String description, double reward, String imageUrl, String actionUrl){
        this.id=id;
        this.title=title;
        this.description=description;
        this.reward=reward;
        this.imageUrl=imageUrl;
        this.actionUrl=actionUrl;
        this.isActive=true;
    }
    // Getters and Setters
    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public double getReward(){return reward;}
    public void setReward(double reward){this.reward=reward;}

    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}

    public String getActionUrl(){return actionUrl;}
    public void setActionUrl(String actionUrl){this.actionUrl=actionUrl;}

    public boolean isActive(){return isActive;}
    public void setActive(boolean isActive){this.isActive=isActive;}

}
