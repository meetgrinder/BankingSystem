package bank.strategies;

import bankeventing.EventObject;

public class ConstantRateStrategy implements TellerStrategizer {

    public int strategize() {
        System.out.println("strategizing");
        return 1;
    }
}
