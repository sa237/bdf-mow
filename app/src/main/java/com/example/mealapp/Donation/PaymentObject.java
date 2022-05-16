package com.example.mealapp.Donation;

public class PaymentObject {

    private String date;
    private String amount;
    private String paymentID;


    public PaymentObject(String date, String amount, String paymentID) {
        this.date = date;
        this.amount = amount;
        this.paymentID = paymentID;


    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }



}
