package bankeventing;

public class EventCustomerLost extends EventObject {
    public int onDutyTellers;
    public int occuranceTimestampMillis;

    public EventCustomerLost() {
        super();
        eventType = BankEventBus.EVENT_CUSTOMER_LOST;
    }
}
