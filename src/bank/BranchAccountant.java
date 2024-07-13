package bank;

import bankeventing.EventObject;
import jdk.jfr.Event;

//TODO -
// this class could handle all cost related events
// customer lost, tellers called in, initial tellers during bank open,
// it could keep a running total of the costs associated with each throughout the day
// on bank close, it could print out a summary report including the customer generation pattern
// and branch officer teller strategy used
// also as different customer needs are added, the customer lost costs could be a map
// of costs for each of those
public class BranchAccountant {

    private void handleCustomerLost(EventObject eventObject) {

    }

    private void handleBankClosed(EventObject eventObject) {
        this.generateCostSummaryReport();
    }

    private void generateCostSummaryReport() {

    }
}
