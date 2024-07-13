package bank;
import bankeventing.BankEventBus;
import bankeventing.EventCallInTellers;
import bankeventing.EventObject;
import customermgmt.BasicCustomerGenerator;

import java.util.concurrent.*;

public class BankBranch {
    private int onDutyTellers = BankSystemConfiguration.numStartingTellers;
    private final int queueSize = BankSystemConfiguration.bankQueueSize;
    public final ArrayBlockingQueue<String> customerQueue = new ArrayBlockingQueue<String>(queueSize, true);
    private Thread customerGeneratorThread;
    private ThreadPoolExecutor tellerExecutors;

    //Has EventSink methods for handling bank decisions
    //these methods are registered in the BankEventBus
    //it has a reference to this BankBranch so that it knows the state info and can calculate decisions
    private BranchOfficer branchOfficer = new BranchOfficer(this);
    private BranchAccountant branchAccountant = new BranchAccountant(); // needs implementation

    public BankBranch() {
        BankEventBus.getInstance().register(BankEventBus.EVENT_BANK_CLOSED, this::handleBankClose);
        BankEventBus.getInstance().register(BankEventBus.EVENT_BANK_OPENED, this::handleBankOpened);
        BankEventBus.getInstance().register(BankEventBus.EVENT_CALL_IN_TELLERS, this::handleCallInTellers);
    }

    public void runBank() {
        System.out.println("Entered BankBranch runBank");
        // tellerExecutors = (ThreadPoolExecutor) Executors.newFixedThreadPool(onDutyTellers) ;
        tellerExecutors = new ThreadPoolExecutor(onDutyTellers, onDutyTellers, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1_000));
        //try (ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(onDutyTellers)) {
        for (int i = 0; i < onDutyTellers; i++) {
            System.out.println("starting teller thread");
            tellerExecutors.execute(new Teller(this.customerQueue));
        }
    }

    public void handleCallInTellers(EventObject eventObject) {
        System.out.println("Entered handleCallInTellers for: " + eventObject.getEventType());
        EventCallInTellers eventCallInTellers = (EventCallInTellers) eventObject;
        System.out.println("call in: " + eventCallInTellers.getNumTellersToCall());
        System.out.println("There are currently this many working tellers: " + tellerExecutors.getCorePoolSize());
        System.out.println("Max num tellerExecutors: " + tellerExecutors.getMaximumPoolSize());
        //tellerExecutors.setCorePoolSize(tellerExecutors.getCorePoolSize() + eventCallInTellers.getNumTellersToCall());
        int numTellers = tellerExecutors.getCorePoolSize() + 1;
        tellerExecutors.setMaximumPoolSize(numTellers);
        tellerExecutors.setCorePoolSize(numTellers);
        for (int i = 0; i < numTellers; i++) {
            System.out.println("starting new teller thread");
            tellerExecutors.execute(new Teller(this.customerQueue));
        }
    }

    //TODO - this should probably become a EVENT_BANK_OPEN handler for that future event
    // EventObject could possibly constain the desired customer traffic type rather than BasicCustomerGenerator
    // and be moved out of this BankBranch class
    public void handleBankOpened(EventObject eventObject) {
        System.out.println("Entered handleBankOpened. Time to start generating customers: ");
        customerGeneratorThread = new Thread(new BasicCustomerGenerator(this.customerQueue));
        customerGeneratorThread.start();
    }

    public void handleBankClose(EventObject eventObject) {
        //TODO - need to drain the customer queue first
        System.out.println("BankBranch is handleBankClose with customerQueue.size(): " + customerQueue.size());
        while(!this.customerQueue.isEmpty()) {
            //System.out.println("BankBranch is handleBankClose with customerQueue.size(): " + customerQueue.size());
            Thread.yield();
        }

        // Disable new tasks from being submitted
        tellerExecutors.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!tellerExecutors.awaitTermination(1, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks forcefully
                tellerExecutors.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                if (!tellerExecutors.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ex) {
            // (Re-)Cancel if current thread also interrupted
            tellerExecutors.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        System.out.println("Shutting down the tellers executor pool");
    }
}
