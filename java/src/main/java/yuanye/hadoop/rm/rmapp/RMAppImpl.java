package yuanye.hadoop.rm.rmapp;

import yuanye.hadoop.event.Dispatcher;
import yuanye.hadoop.event.EventHandler;
import yuanye.hadoop.fsm.SingleArcTransition;
import yuanye.hadoop.fsm.StateMachineFactory;
import yuanye.hadoop.rm.rmapp.attempt.RMAppAttempt;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kali on 9/17/14.
 */
public class RMAppImpl implements RMApp,EventHandler<RMAppEvent> {

    private final Dispatcher dispatcher ;
    private final long appID;
    private final Map<Long,RMAppAttempt> attempts = new LinkedHashMap<>();


    private final StateMachineFactory<RMAppImpl,RMAppState,RMAppEventType,RMAppEvent> stateMachineFactory =
            new StateMachineFactory<RMAppImpl,RMAppState,RMAppEventType,RMAppEvent>(RMAppState.NEW)

            //from RMAppState.NEW
            .addTransition(RMAppState.NEW, RMAppState.NEW_SAVING,
                    RMAppEventType.APP_SAVED, new RMAppSavingTransition())



            .installTopology();


    public RMAppImpl(Dispatcher dispatcher,long addID){
        this.dispatcher = dispatcher;
        this.appID = addID;
    }


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


    private class RMAppSavingTransition implements SingleArcTransition<RMAppImpl,RMAppEvent>{
        @Override
        public void transition(RMAppImpl rmApp, RMAppEvent rmAppEvent) {
            System.out.println("RMAppSavingTransition save " + rmApp);
            dispatcher.getEventHandler().handle(new RMAppEvent(RMAppEventType.APP_SAVED,appID));
        }
    }

    private class FinalTransition implements SingleArcTransition<RMAppImpl,RMAppEvent>{

        private Set<RMAppAttempt> attempts(RMAppImpl rmApp){
            Set<RMAppAttempt> attempts = new HashSet<>();
            for (RMAppAttempt appAttempt : rmApp.attempts.values()){
                attempts.add(appAttempt);
            }
            return attempts;
        }

        @Override
        public void transition(RMAppImpl rmApp, RMAppEvent rmAppEvent) {
            Set<RMAppAttempt> appAttempts = attempts(rmApp);

        }
    }
}
