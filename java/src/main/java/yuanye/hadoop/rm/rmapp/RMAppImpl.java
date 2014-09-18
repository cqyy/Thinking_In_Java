package yuanye.hadoop.rm.rmapp;

import yuanye.hadoop.event.EventHandler;
import yuanye.hadoop.rm.rmapp.rmappattempt.RMAppAttempt;

import java.util.Map;

/**
 * Created by Kali on 9/17/14.
 */
public class RMAppImpl implements RMApp,EventHandler<RMAppEvent> {
    @Override
    public long getApplicationID() {
        return 0;
    }

    @Override
    public RMAppState getState() {
        return null;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public long getSubmitTime() {
        return 0;
    }

    @Override
    public long getStartTime() {
        return 0;
    }

    @Override
    public long getFinishTime() {
        return 0;
    }

    @Override
    public RMAppAttempt getCurrentAppAttempt() {
        return null;
    }

    @Override
    public Map<Long, RMAppAttempt> getAppAttempts() {
        return null;
    }

    @Override
    public void handle(RMAppEvent event) {

    }
}
