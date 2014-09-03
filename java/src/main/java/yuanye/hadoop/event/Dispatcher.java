package yuanye.hadoop.event;

import yuanye.hadoop.service.Service;

/**
 * Created by Kali on 14-9-3.
 */
public interface Dispatcher extends Service {

    EventHandler getEventHandler();

    void registerEventHandler(Class<? extends Enum> eventType,EventHandler handler);
}
