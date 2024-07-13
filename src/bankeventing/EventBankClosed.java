package bankeventing;

public class EventBankClosed extends EventObject {

    public EventBankClosed() {
        eventType = BankEventBus.EVENT_BANK_CLOSED;
    }
    public String getEventType() {
        return this.eventType;
    }
}

