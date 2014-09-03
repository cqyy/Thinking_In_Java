package yuanye.hadoop.event;

import yuanye.hadoop.service.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Kali on 14-9-3.
 */
public class AsyncDispatcher implements Dispatcher,Service {

    //Map from event type name to handler
    private final Map<Class<? extends Enum>,EventHandler> handlerMap = new HashMap<>();
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final int handlers;
    private final Thread[] handlerThreads;

    public AsyncDispatcher(int size){
        handlers = size;
        handlerThreads = new Thread[handlers];
    }

    @Override
    public EventHandler getEventHandler() {
        return new GenericEventHandler<>();
    }

    @Override
    public void registerEventHandler(Class<? extends Enum> eventType, EventHandler handler) {
        synchronized (handlerMap){
            if (handlerMap.get(eventType) == null){
                handlerMap.put(eventType,handler);
            }
        }
    }

    @Override
    public void start() {
        for(Thread thread : handlerThreads){
            thread.start();
        }
    }

    @Override
    public void init() {
        for(int i = 0; i < handlerThreads.length; i++){
            handlerThreads[i] = new Thread(createHandThread());
        }
    }

    @Override
    public void stop() {
        for(Thread thread : handlerThreads){
            thread.interrupt();
        }
    }

    protected void dispath(Event event){
        synchronized (handlerMap){
            EventHandler handler = handlerMap.get(event.getType().getDeclaringClass());
            if (handler == null){
                throw new RuntimeException("No handler found for event: " + event);
            }
            handler.handle(event);
        }
    }

    protected Runnable createHandThread(){
        return ()->{
          while (!Thread.currentThread().isInterrupted()){
              try {
                  Event event = eventQueue.take();
                  dispath(event);
              } catch (InterruptedException e) {
                  //if.exit.
                  continue;
              }
          }
        };
    }

    private  class GenericEventHandler<E extends Event> implements EventHandler<E>{
        @Override
        public void handle(E event) {
            synchronized (eventQueue){
                try {
                    eventQueue.put(event);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
