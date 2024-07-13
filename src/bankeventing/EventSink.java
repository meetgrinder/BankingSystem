package bankeventing;

// this is a functional interface
// the implementers will be function references so that a single class can
// register and handle multiple event types
public interface EventSink {
    void handleIt(EventObject eventObject);
}

