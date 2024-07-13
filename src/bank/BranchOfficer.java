package bank;
import bank.strategies.ConstantRateStrategy;
import bank.strategies.TellerStrategizer;
import bankeventing.BankEventBus;
import bankeventing.EventCallInTellers;
import bankeventing.EventObject;

public class BranchOfficer {
    private BankBranch bankBranch;
    private TellerStrategizer tellerStrategizer;

    public BranchOfficer(BankBranch bankBranch) {
        this.bankBranch = bankBranch;

        //new types of strategies could be added.
        //this could become an enum
        //this could also turn into more of an IoC framework where this BranchOffice is passed in this instance
        //rather than creating it itself
        //TODO - if there arent security concerns the config could also contain a string of the class
        //TODO - if that string was a path, then a classloader could instantiate it
        if(BankSystemConfiguration.tellerStrategizer.equals("constantRateStrategy")) {
            tellerStrategizer = new ConstantRateStrategy();
        }
        BankEventBus.getInstance().register(BankEventBus.EVENT_CUSTOMER_LOST, this::handleCustomerLost);
    }

    //EventSink function
    public void handleCustomerLost(EventObject eventObject) {
        System.out.println("Damnit we lost one!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        int numTellersToCallIn = tellerStrategizer.strategize();
        if (numTellersToCallIn > 0) {
            System.out.println("call tellers: " + numTellersToCallIn);
            BankEventBus.getInstance().addEvent(new EventCallInTellers(numTellersToCallIn));
        }
        else {
            System.out.println("not calling in tellers yet");
        }
    }
}

