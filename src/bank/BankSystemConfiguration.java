package bank;

//TODO - this should eventually be replaced by a configuration file
// e.g. a json file to allow for more complex configuration passed into the program
// and would allow more detailed settings for individual configuration items
public class BankSystemConfiguration {
    public static final int timeToRun = 30000;
    public static final int numStartingTellers = 1;
    public static final int customerServiceTime = 5000;
    public static final int bankQueueSize = 100;
    public static final String tellerStrategizer = "constantRateStrategy";
}
