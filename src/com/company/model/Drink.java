package com.company.model;

import org.json.JSONObject;

public class Drink {

    private String name;
    private int amountLeft;
    private double price;
    private JSONObject drinkObject;
    private int jsonArrayIndex;

    public Drink(JSONObject drinkObject, String name, int quantity, double price, int jsonArrayIndex) {
        this.drinkObject = drinkObject;
        this.name = name;
        this.amountLeft = quantity;
        this.price = price;
        this.jsonArrayIndex = jsonArrayIndex;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountLeft() {
        return amountLeft;
    }

    public int getJsonArrayIndex() {
        return jsonArrayIndex;
    }

    public boolean isDrinkAvailable() {
        return amountLeft > 0;
    }

    public void updateObject() {
        amountLeft--;
        drinkObject.put("quantity", amountLeft);
    }

    public JSONObject getDrinkObject() {
        return drinkObject;
    }
}
