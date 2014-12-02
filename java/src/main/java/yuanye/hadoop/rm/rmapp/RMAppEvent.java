package yuanye.hadoop.rm.rmapp;

import yuanye.hadoop.event.AbstractEvent;
<<<<<<< HEAD
import yuanye.hadoop.rm.rmapp.attempt.RMAppEventType;
=======
>>>>>>> b0552bb38ad7f6dbd87da6328e6d5afab9ccab55

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
