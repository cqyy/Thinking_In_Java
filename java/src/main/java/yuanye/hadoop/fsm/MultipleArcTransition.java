package yuanye.hadoop.fsm;

/**
 * Created by Administrator on 2014/9/4.
 */
public interface MultipleArcTransition<OPERAND,EVENT,STATE extends Enum<STATE>> {
    STATE transition(OPERAND operand,EVENT event);
}
