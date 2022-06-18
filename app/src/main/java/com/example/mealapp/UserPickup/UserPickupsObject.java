package com.example.mealapp.UserPickup;

import java.util.Comparator;

public class UserPickupsObject {

    private String date;
    private String noOfMeals;
    private String food;
    private String id;
    private String location;

    public UserPickupsObject(String date,String noOfMeals, String food,String id,String location){
        this.date = date;
        this.noOfMeals = noOfMeals;
        this.food = food;
        this.id = id;
        this.location = location;
    }

    public String getNoOfMeals() { return noOfMeals; }

    public String getDate() {
        return date;
    }

    public String getFood() {
        return food;
    }

    public String getId() { return id; }

    public void setNoOfMeals(String noOfMeals) { this.noOfMeals = noOfMeals; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setId(String id) { this.id = id; }

    public void setLocation(String location) { this.location = location; }

    public String getLocation() { return location; }



}


