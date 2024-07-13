import bank.BankBranch;
import bank.BankSystemConfiguration;
import bankeventing.BankEventBus;
import bankeventing.EventBankClosed;
import bankeventing.EventBankOpened;

public class Main {
    public static volatile boolean isOpen = true;
    BankEventBus bankEventBus = BankEventBus.getInstance(); // A self-starting Thread!!!
    BankBranch bankBranch;
    public Main() {
        //open properties file that will set the events and strategy(s)
        //customer generator type
    }

    public void runBankLocation() {
        try {
            bankEventBus.printListOfEvents();
            bankBranch = new BankBranch();
            bankBranch.runBank(); //this just lets the tellers get up and ready
            BankEventBus.getInstance().addEvent(new EventBankOpened());
            Thread.sleep(BankSystemConfiguration.timeToRun);
            BankEventBus.getInstance().addEvent(new EventBankClosed());
            BankEventBus.getInstance().shouldTerminate = true;
        }
        catch(InterruptedException e) {
            System.out.println("Exception in runBankLocation");
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        //initialize and configure strategy(s)
        //that will handle events
        Main mainBank = new Main();
        mainBank.runBankLocation();

        //TODO - create a thread for user input which would generate an event
        //to generate a cost summary report from the accountant at that moment
        //this would be an additional event that the accountant could respond to
        System.out.println("Exiting main");
    }
}