package yuanye.hadoop.rm.rmapp;

import yuanye.hadoop.rm.rmapp.attempt.RMAppAttempt;

import java.util.Map;

/**
 * Created by Kali on 9/17/14.
 */
public interface RMApp {

    long getApplicationID();

    RMAppState getState();

    float getProgress();

    String getName();

    long getSubmitTime();
    long getStartTime();
    long getFinishTime();

    RMAppAttempt getCurrentAppAttempt();

    Map<Long,RMAppAttempt> getAppAttempts();
}
