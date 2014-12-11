package yuanye.hadoop.rm.rmapp;

/**
 * Created by Kali on 9/17/14.
 */
public enum  RMAppState {
        NEW,
        NEW_SAVING,
        SUBMITTED,
        ACCEPTED,
        RUNNING,
        REMOVING,
        FINISHING,
        FINISHED,
        FAILED,
        KILLED
}
