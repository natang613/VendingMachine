///
//  In order to run you have to put the path to the json database in the command line
//  I used a json file as my database
//  Created by Natan Ginsberg
///
package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String CONTINUE_PROMPT = "Would you like to place another order? y/n";
    private static final String CONTINUE_ERROR = "Invalid input, only \"y\" or \"n\" are valid inputs";
    private static final String PURCHASE_PROMPT = "What drink would you like to order? ";
    private static final String INVALID_COIN_INPUT = "Invalid input, you must input an integer greater or equal to zero.";
    private static final String OUT_OF_ORDER = "Out of Order";
    private static final String NOT_ENOUGH_ARGS = "Please enter the json database file at runtime";
    private static final String FILE_NOT_FOUND = "Please enter a valid file";

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                Machine machine = new Machine(args[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    getDrinks(reader, machine);
                    getCoins(reader, machine);
                    try {
                        machine.handlePurchase();
                        machine.updateInventory();
                        System.out.println("Here is your " + machine.getDrinkWanted() + "\nThank you for your order.\n" + machine.getChange());
                    } catch (NotEnoughMoney enoughMoney) {
                        System.out.println(enoughMoney.getMessage());
                    } catch (NotCorrectAmountOfChange change) {
                        System.out.println(change.getMessage() + " " + machine.getCoinsInserted());
                    }
                    // In case an IO error occurs on the json file we want to close the reader
                    catch (IOException e) {
                        System.out.println(OUT_OF_ORDER);
                        reader.close();
                    }
                    boolean continueOrNot = continuePrompt(reader);
                    if (!continueOrNot)
                        break;
                }
                reader.close();
            } else {
                System.out.println(NOT_ENOUGH_ARGS);
            }
        } catch (FileNotFoundException e) {
            System.out.println(FILE_NOT_FOUND);
        } catch
        (IOException e) {
            System.out.println(OUT_OF_ORDER);
        }
    }

    private static boolean continuePrompt(BufferedReader reader) throws IOException {
        while (true) {
            System.out.print(CONTINUE_PROMPT);
            String continueOrNot = reader.readLine();
            if (continueOrNot.equals("y") || continueOrNot.equals("n")) {
                return continueOrNot.equals("y");
            } else {
                System.out.println(CONTINUE_ERROR);
            }
        }
    }

    private static void getCoins(BufferedReader reader, Machine machine) throws IOException {
        List<Double> coinOptions = machine.getCoinOptions();
        Map<Double, Integer> inputCoins = new HashMap<>();
        for (double coinOption : coinOptions) {
            int coinAmount = getCoinAmountFromUser(coinOption, reader);
            inputCoins.put(coinOption, coinAmount);
        }
        machine.addInputCoins(inputCoins);
    }

    private static int getCoinAmountFromUser(double coinOption, BufferedReader reader) throws IOException {
        while (true) {
            System.out.print("How many " + coinOption + " coins are you using? ");
            String coinsGiven = reader.readLine();
            if (isPositiveNumeric(coinsGiven)) {
                return Integer.parseInt(coinsGiven);
            } else {
                System.out.println(INVALID_COIN_INPUT);
            }

        }
    }

    public static boolean isPositiveNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
            return d >= 0;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static void getDrinks(BufferedReader reader, Machine machine) throws IOException {
        boolean drinkRequestAvailable = false;
        while (!drinkRequestAvailable) {
            System.out.print(PURCHASE_PROMPT);
            String drinkRequested = reader.readLine();
            try {
                if (machine.setWantedDrinkIfAvailable(drinkRequested)) {
                    drinkRequestAvailable = true;

                } else {
                    System.out.println(drinkRequested + " is out of stock");
                }
            } catch (DrinkNotFound drinkError) {
                System.out.println(drinkError.getMessage());
            }
        }
    }
}
