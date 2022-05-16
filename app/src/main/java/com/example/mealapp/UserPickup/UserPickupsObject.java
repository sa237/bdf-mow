package com.example.mealapp.UserPickup;

public class UserPickupsObject {

    private String date;
    private String noOfMeals;
    private String food;
    private String id;

    public UserPickupsObject(String date,String noOfMeals, String food,String id){
        this.date = date;
        this.noOfMeals = noOfMeals;
        this.food = food;
        this.id = id;
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
}
