package com.example.mealapp;

public class MyModel {

    private String title,description,clickableText;
    private int image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClickableText() {
        return clickableText;
    }

    public void setClickableText(String clickableText) {
        this.clickableText = clickableText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public MyModel(String title, String description, String clickableText, int image) {
        this.title = title;
        this.description = description;
        this.clickableText = clickableText;
        this.image = image;


    }
}
