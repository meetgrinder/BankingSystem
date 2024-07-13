package bankeventing;

public class EventCallInTellers extends EventObject {
    private int numTellersToCall;

    public EventCallInTellers(int numTellers) {
        super();
        this.eventType = BankEventBus.EVENT_CALL_IN_TELLERS;
        this.numTellersToCall = numTellers;
    }

    public int getNumTellersToCall() {
        return this.numTellersToCall;
    }

    public String getEventType() {
        return this.eventType;
    }
}

