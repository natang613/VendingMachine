package com.company.model;

import org.json.JSONObject;

public class Coin implements Comparable<Coin> {

    private int quantity;
    private double value;
    private int changeToReturn = 0;
    private int jsonArrayIndex;
    private JSONObject coinObject;
    private int amountOfCoinInserted = 0;

    @Override
    public int compareTo(Coin e) {
        return Double.compare(this.getValue(), e.getValue());
    }

    public Coin(JSONObject coinObject, double value, int quantity, int jsonArrayIndex) {
        this.coinObject = coinObject;
        this.value = value;
        this.quantity = quantity;
        this.jsonArrayIndex = jsonArrayIndex;
    }

    public double calculateChange(double amount) {
        // if need be returning coins that were just inserted
        for (int i = 1; i <= quantity + amountOfCoinInserted + 1; i++) {
            if (i * value <= amount) {
                changeToReturn++;
            }
        }
        return changeToReturn * value;
    }

    public int getJsonArrayIndex() {
        return jsonArrayIndex;
    }

    public double getValue() {
        return value;
    }

    public int getChangeToReturn() {
        return changeToReturn;
    }

    public JSONObject getCoinObject() {
        return coinObject;
    }

    public void updateObject() {
        quantity = quantity - changeToReturn + amountOfCoinInserted;
        coinObject.put("quantity", quantity);
        removeCoinsInserted();
    }

    public void changeReturned() {
        changeToReturn = 0;
    }

    public int getAmountOfCoinInserted() {
        return amountOfCoinInserted;
    }

    public void setAmountOfCoinInserted(int amountOfCoinInserted) {
        this.amountOfCoinInserted = amountOfCoinInserted;
    }

    public void removeCoinsInserted() {
        amountOfCoinInserted = 0;
    }
}
