package yuanye.hadoop.fsm;

/**
 * Created by Administrator on 2014/9/4.
 */
public interface StateMachine<STATE extends Enum<STATE>,EVENTTYPE extends Enum<EVENTTYPE>,EVENT> {
    STATE getCurrentState();
    STATE doTransition(EVENTTYPE eventType,EVENT event);
}
