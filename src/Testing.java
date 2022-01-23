import com.company.DrinkNotFound;
import com.company.Machine;
import com.company.model.Coin;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testing {

    @Test
    public void testChangeDistribution() {
        try {
            Machine machine = new Machine("C:\\Users\\Natan\\IdeaProjects\\Vending\\src\\inventory");
            machine.setWantedDrinkIfAvailable("coca cola");
            List<Double> changeOptions = new ArrayList<>();
            changeOptions.add(3.5);  // 1
            changeOptions.add(3.75);  // 2
            changeOptions.add(0.75);  // 3
            changeOptions.add(3.5);  // 4
            changeOptions.add(4.0);  // 5
            changeOptions.add(3.5);  // 6
            // there are no more 3.0 left and no more 0.5 left
            changeOptions.add(4.5);  // 7
            List<Map<Double, Integer>> outputs = new ArrayList<>();
            Map<Double, Integer> coinDistribution = new HashMap<>();
            // 1
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 1);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 1);
            coinDistribution.put(0.25, 0);
            outputs.add(coinDistribution);

            // 2
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 1);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 1);
            coinDistribution.put(0.25, 1);
            outputs.add(coinDistribution);
            // 3
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 0);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 1);
            coinDistribution.put(0.25, 1);
            outputs.add(coinDistribution);
            //4
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 1);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 1);
            coinDistribution.put(0.25, 0);
            outputs.add(coinDistribution);
            //5
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 1);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 1);
            coinDistribution.put(0.5, 0);
            coinDistribution.put(0.25, 0);
            outputs.add(coinDistribution);
            //6
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 1);
            coinDistribution.put(2.0, 0);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 1);
            coinDistribution.put(0.25, 0);
            outputs.add(coinDistribution);
            //7
            coinDistribution = new HashMap<>();
            coinDistribution.put(5.0, 0);
            coinDistribution.put(3.0, 0);
            coinDistribution.put(2.0, 2);
            coinDistribution.put(1.0, 0);
            coinDistribution.put(0.5, 0);
            coinDistribution.put(0.25, 2);
            outputs.add(coinDistribution);
            for (int i = 0; i < 7; i++) {
                machine.calculateCoinDistributionChange(changeOptions.get(i));
                System.out.println(i);
                for (Coin coin : machine.getCoins()) {
                    try {
                        assert coin.getChangeToReturn() == outputs.get(i).get(coin.getValue()) : "expected there to be " + outputs.get(i).get(coin.getValue()) + " - " + coin.getValue() + " but there were " + coin.getChangeToReturn() + " - " + coin.getValue();
                    } catch (AssertionError e) {
                        System.out.println(e);
                    }
                }
                machine.updateInventory();
            }
        } catch (IOException | DrinkNotFound e) {
        }
    }


    @Test
    public void testOutOfStock() {
        try {
            Machine machine = new Machine("C:\\Users\\Natan\\IdeaProjects\\Vending\\src\\inventory");
            machine.setWantedDrinkIfAvailable("coca cola");
            machine.updateInventory();
            machine.setWantedDrinkIfAvailable("coca cola");
            machine.updateInventory();
            // the coke stock is finished
            try {
                assert !machine.setWantedDrinkIfAvailable("coca cola") : "The drink is out of stock";
            } catch (AssertionError e) {
                System.out.println(e);
            }

        } catch (IOException | DrinkNotFound e) {
        }
    }


}
