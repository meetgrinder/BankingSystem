package customermgmt;
import bankeventing.BankEventBus;
import bankeventing.EventCustomerLost;
import bankeventing.EventObject;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class BasicCustomerGenerator implements Runnable {
    // TODO - handle future events eg BANK_OPEN and BANK_CLOSE

    boolean isOpen = true;
    Random r = new Random();
    int min = 100;
    int max = 100;
    private final ArrayBlockingQueue<String> bankBranchCustomerQueue;

    public BasicCustomerGenerator(ArrayBlockingQueue<String> bankBranchCustomerQueue) {
        this.bankBranchCustomerQueue = bankBranchCustomerQueue;
        BankEventBus.getInstance().register(BankEventBus.EVENT_BANK_CLOSED, this::handleBankClose);
    }

    private void generateCustomerEvent() {
        try {
            this.bankBranchCustomerQueue.add("CUSTOMER");
        }
        catch(IllegalStateException e) {
            EventCustomerLost eventCustomerLost = new EventCustomerLost();
            BankEventBus.getInstance().addEvent(eventCustomerLost);
        }
    }

    public void handleBankClose(EventObject eventObject) {
        this.isOpen = false;
    }

    @Override
    public void run() {
        System.out.println("started BasicCustomerGenerator thread");
        int randomNumber;

        while(isOpen == true) {
            randomNumber = r.nextInt((max - min) + 1) + min;
            try {
                Thread.sleep(randomNumber);
                this.generateCustomerEvent();
            }
            catch (InterruptedException e) {
                System.out.println("BasicCustomerGenerator");
            }
        }
        System.out.println("Shutting down the customer generator");
    }
}

