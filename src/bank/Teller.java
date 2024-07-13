package bank;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Teller implements Runnable {

    private final ArrayBlockingQueue<String> bankQueue;

    public Teller(ArrayBlockingQueue<String> bankQueue) {
        this.bankQueue = bankQueue;
        System.out.println("Entered Teller Constructor");
    }

    //TODO - this is blocking without timeouts so there is no way of managing idle time yet
    private void handleCustomer() {
        String customer;
        try {
            while (true) {
                //TODO - at some point different types of customers needs and different amount of time
                // to service that need.
                // that would replace the generic tellerHandlingTime
                customer = this.bankQueue.take();
                System.out.println("Teller is handling a customer");
                Thread.sleep(BankSystemConfiguration.customerServiceTime);
                //System.out.println("Teller is done handling a customer");
            }
        }
        catch (InterruptedException e) {
            System.out.println("teller had InterruptedException");
        }
    }

    @Override
    public void run() {
        System.out.println("Started Teller thread");
        this.handleCustomer();
    }
}
