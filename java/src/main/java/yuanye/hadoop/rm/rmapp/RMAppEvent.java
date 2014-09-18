package yuanye.hadoop.rm.rmapp;

import yuanye.hadoop.event.AbstractEvent;

/**
 * Created by Kali on 9/17/14.
 */
public class RMAppEvent extends AbstractEvent<RMAppEventType> {

    private final long appId;

    public RMAppEvent(RMAppEventType rmAppEventType,long appId) {
        super(rmAppEventType);
        this.appId = appId;
    }

    public long getRMAppID(){
        return appId;
    }
}
