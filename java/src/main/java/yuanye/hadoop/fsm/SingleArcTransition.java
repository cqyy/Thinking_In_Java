package yuanye.hadoop.fsm;

/**
 * Created by Administrator on 2014/9/4.
 */
public interface SingleArcTransition<OPERAND,EVENT> {
    void transition(OPERAND operand,EVENT event);
}
