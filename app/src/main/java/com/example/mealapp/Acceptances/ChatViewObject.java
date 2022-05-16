package com.example.mealapp.Acceptances;

public class ChatViewObject {

    private String number;
    private String name;
    private String id;

    public ChatViewObject(String number, String name , String id) {
        this.number = number;
        this.name = name;
        this.id = id;
    }

    public String getNumber() { return number; }

    public String getName() { return name; }

    public String getId(){
        return id;
    }
}
