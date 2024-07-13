package bankeventing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BankEventBus extends Thread {
    public boolean shouldTerminate = false;
    //{EventTypeString, ArrayList} of registered objects for that eventtype
    private final HashMap<String, ArrayList<EventSink>> eventRegistry = new HashMap<>();
    private final ArrayBlockingQueue<EventObject> eventQueue = new ArrayBlockingQueue<EventObject>(10000000, true);
    public static String EVENT_CUSTOMER_LOST = "EVENT_CUSTOMER_LOST";
    public static String EVENT_CALL_IN_TELLERS = "EVENT_CALL_TELLERS";
    public static String EVENT_BANK_CLOSED = "EVENT_BANK_CLOSED";
    public static String EVENT_BANK_OPENED = "EVENT_BANK_OPENED";

    public static ArrayList<String> eventTypes;
    private ThreadPoolExecutor eventExecutorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10) ;

    private static class LazyHolder {
        private static final BankEventBus INSTANCE = new BankEventBus();
    }

    private BankEventBus() {
        eventTypes = new ArrayList<>(Arrays.asList(EVENT_CUSTOMER_LOST,
                EVENT_CALL_IN_TELLERS,
                EVENT_BANK_CLOSED,
                EVENT_BANK_OPENED));
        for (String e: BankEventBus.eventTypes) {
            eventRegistry.put(e, new ArrayList<EventSink>());
        }
        this.start();
    }

    public void printListOfEvents() {
        for (String eventType: eventTypes) {
            System.out.println("eventType: " + eventType);
        }
    }

    public static BankEventBus getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void register(String eventType, EventSink eventSink ) {
        ArrayList<EventSink> eventSinks = this.eventRegistry.get(eventType);
        eventSinks.add(eventSink);
    }

    public void addEvent(EventObject eventObject) {
        //System.out.println("adding event " + eventObject.getEventType());
        this.eventQueue.add(eventObject);
    }

    public void run() {
        System.out.println("Entered BankEventBus thread run");
        //note: declaring it inside the while loop instead of here, makes the referende "effectively final"
        //and therefor allowed in the lambda
        //EventObject eventObject;
        try {
            while(!this.shouldTerminate) {
                System.out.println("eventbus looping");
                EventObject eventObject = eventQueue.take();
                System.out.println("getting registry for: " + eventObject.eventType);
                ArrayList<EventSink> eventSinks = eventRegistry.get(eventObject.eventType);
                for(EventSink es: eventSinks) {
                    //dispatch to each event handler using a threadpool rather than doing syncronously in single thread
                    //es.handleIt(eventObject);
                    Runnable runnableTask = () -> {
                        es.handleIt(eventObject);
                    };
                    this.eventExecutorPool.execute(runnableTask);
                }

                Thread.yield();
                //Thread.sleep(10000);
            }
            System.out.println("Shutting down the BankEventBus");
        }
        catch (InterruptedException e) {
            System.out.println("EventBus has died");
        }
    }
}

