package com.company;

import com.company.model.Coin;
import com.company.model.Drink;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Machine {

    private static final String DRINK_NAME = "drinks";
    private static final String COIN_NAME = "coins";
    private static final String CHANGE_STRING_PREFIX = "Here is your change ";

    private List<Coin> coins = new ArrayList<>();
    List<Drink> drinks = new ArrayList<>();
    JSONArray jsonArrayCoins;
    JSONArray jsonArrayDrinks;
    Drink wantedDrink;
    private JSONObject machine;
    String jsonPath;

    public Machine(String jsonPath) throws IOException {
        this.jsonPath = jsonPath;
        initiateObjects(jsonPath);
    }

    private void initiateObjects(String jsonPath) throws IOException {
        InputStream is = new FileInputStream(jsonPath);
        JSONTokener tokener = new JSONTokener(is);
        machine = new JSONObject(tokener);
        addDrinks(machine);
        addCoins(machine);
        is.close();
    }

    private void addDrinks(JSONObject object) {
        jsonArrayDrinks = object.getJSONArray(DRINK_NAME);
        for (int i = 0; i < jsonArrayDrinks.length(); i++) {
            JSONObject drink = jsonArrayDrinks.getJSONObject(i);
            this.drinks.add(new Drink(drink, drink.getString("name"), drink.getInt("quantity"), drink.getDouble("price"), i));
        }
    }

    private void addCoins(JSONObject object) {
        jsonArrayCoins = object.getJSONArray(COIN_NAME);
        for (int i = 0; i < jsonArrayCoins.length(); i++) {
            JSONObject coin = jsonArrayCoins.getJSONObject(i);
            this.coins.add(new Coin(coin, coin.getDouble("value"), coin.getInt("quantity"), i));
        }
        this.coins.sort(Collections.reverseOrder());
    }

    public void handlePurchase() throws NotEnoughMoney, NotCorrectAmountOfChange {
        double moneyInserted = calculateAmountOfMoneyInserted();
        double changeNeeded = calculateChangeNeededToReturn(moneyInserted);
        if (changeNeeded >= 0) {
            if (calculateCoinDistributionChange(changeNeeded)) {
                return;
            } else {
                throw new NotCorrectAmountOfChange();
            }
        } else {
            throw new NotEnoughMoney();
        }
    }

    private double calculateAmountOfMoneyInserted() {
        double sum = 0;
        for (Coin coin : coins) {
            sum += coin.getValue() * coin.getAmountOfCoinInserted();
        }
        return sum;
    }

    public boolean setWantedDrinkIfAvailable(String request) throws DrinkNotFound {
        for (Drink drink : drinks) {
            if (drink.getName().toLowerCase().equals(request.toLowerCase())) {
                if (drink.isDrinkAvailable()) {
                    wantedDrink = drink;
                    return true;
                } else
                    return false;
            }
        }
        // no drink matched the request
        throw new DrinkNotFound("We do not carry " + request);
    }

    private double calculateChangeNeededToReturn(double amountOfMoneyInserted) {
        return amountOfMoneyInserted - wantedDrink.getPrice();
    }

    public boolean calculateCoinDistributionChange(double changeNeeded) {
        for (Coin coin : coins) {
            changeNeeded -= coin.calculateChange(changeNeeded);
        }
        return changeNeeded == 0;
    }

    public void updateInventory() throws IOException {
        updateDrinks();
        updateCoins();
        updateMachine();
        updateFile();
    }

    private void updateFile() throws IOException {
        Writer writer = new FileWriter(jsonPath);
        machine.write(writer);
        writer.close();
    }

    private void updateMachine() {
        machine.put(DRINK_NAME, jsonArrayDrinks);
        machine.put(COIN_NAME, jsonArrayCoins);
    }

    private void updateCoins() {
        for (Coin coin : coins) {
            coin.updateObject();
            jsonArrayCoins.put(coin.getJsonArrayIndex(), coin.getCoinObject());
        }

    }

    private void updateDrinks() {
        wantedDrink.updateObject();
        jsonArrayDrinks.put(wantedDrink.getJsonArrayIndex(), wantedDrink.getDrinkObject());
    }

    public List<Double> getCoinOptions() {
        List<Double> coinOptions = new ArrayList<>();
        for (Coin coin : coins) {
            coinOptions.add(coin.getValue());
        }
        return coinOptions;
    }

    public void addInputCoins(Map<Double, Integer> inputCoins) {
        for (Coin coin : coins) {
            coin.setAmountOfCoinInserted(inputCoins.get(coin.getValue()));
        }
    }

    public String getCoinsInserted() {
        StringBuilder coinsRequestedString = new StringBuilder();
        for (Coin coin : coins) {
            coinsRequestedString.append(coin.getValue()).append(" - ").append(coin.getAmountOfCoinInserted()).append(" ");
            coin.removeCoinsInserted();
        }
        return coinsRequestedString.toString();
    }

    public String getChange() {
        StringBuilder changeString = new StringBuilder();
        changeString.append(CHANGE_STRING_PREFIX);
        boolean changeNeeded = false;
        for (Coin coin : coins) {
            if (coin.getChangeToReturn() > 0) {
                changeNeeded = true;
                changeString.append(coin.getValue()).append(" - ").append(coin.getChangeToReturn()).append(" ");
                coin.changeReturned();
            }
        }
        return changeNeeded ? changeString.toString() : "";
    }


    public String getDrinkWanted() {
        return wantedDrink.getName();
    }

    // for testing purposes only
    public List<Coin> getCoins() {
        return coins;
    }
}

class NotEnoughMoney extends Exception {
    public NotEnoughMoney() {
        super("Not enough Money was inserted, please try again from the beginning");
    }
}

class NotCorrectAmountOfChange extends Exception {
    public NotCorrectAmountOfChange() {
        super("Not enough change ");
    }
}