package bankeventing;

public class EventBankOpened extends EventObject {
    public EventBankOpened() {
        eventType = BankEventBus.EVENT_BANK_OPENED;
    }
}
