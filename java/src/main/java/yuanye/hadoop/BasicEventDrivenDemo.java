package yuanye.hadoop;

import yuanye.hadoop.event.Event;
import yuanye.hadoop.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kali on 14-9-4.
 */
public class BasicEventDrivenDemo {

    private static interface Dispatcher {

        EventHandler getEventHandler(Class<? extends Enum> type);

        void registerEventHandler(Class<? extends Enum> eventType,EventHandler handler);
    }

    private static enum EventType{
        E_START,
        E_STOP;
    }

    private static class MyEvent implements Event<EventType>{
        private final EventType type;
        public MyEvent(EventType type){
            this.type = type;
        }
        @Override
        public EventType getType() {
            return type;
        }

        @Override
        public String toString() {
            return "MyEvent: " + type;
        }
    }
    private static class MyDispatcher implements Dispatcher{
        private final Map<Class<? extends Enum>,EventHandler<MyEvent>> handlers;

        public MyDispatcher(){
            handlers = new HashMap<>();
        }

        @Override
        public EventHandler getEventHandler(Class<? extends Enum> type) {
            synchronized (handlers){
                return handlers.get(type);
            }
        }

        @Override
        public void registerEventHandler(Class<? extends Enum> eventType, EventHandler handler) {
            synchronized (handlers){
                handlers.put(eventType,handler);
            }
        }
    }

    private static class MyEventHandler implements EventHandler<MyEvent>{
        @Override
        public void handle(MyEvent event) {
            System.out.println("MyEventHandler is handling " + event);
        }
    }

    public static void main(String[] args) {
        MyDispatcher dispatcher = new MyDispatcher();
        dispatcher.registerEventHandler(EventType.class,new MyEventHandler());
        dispatcher.getEventHandler(EventType.class).handle(new MyEvent(EventType.E_START));
        dispatcher.getEventHandler(EventType.class).handle(new MyEvent(EventType.E_STOP));
    }
}
