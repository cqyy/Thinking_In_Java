package yuanye.hadoop.fsm;

import java.util.*;

/**
 * Created by Administrator on 2014/9/4.
 */
public class StateMachineFactory<OPERAND,STATE extends Enum<STATE>,EVENTTYPE extends Enum<EVENTTYPE>,EVENT> {

    private Map<STATE,Map<EVENTTYPE,Transition<OPERAND,STATE,EVENTTYPE,EVENT>>> transitionTopology;
    private TransitionListNode listNode;
    private STATE defaultInitState;

    public StateMachineFactory(STATE defaultInitState){
        this.defaultInitState = defaultInitState;
    }

    private StateMachineFactory(StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> that,
                                ApplicableSingleOrMutipleArcTransition<OPERAND,STATE,EVENTTYPE,EVENT> transition) {
        this.listNode = new TransitionListNode(transition,that.listNode);
        this.defaultInitState = that.defaultInitState;
    }

    private interface Transition<OPERAND,STATE extends Enum<STATE>,EVENTTYPE extends Enum<EVENTTYPE>,EVENT>{
        STATE doTransition(OPERAND operand,STATE preState,EVENT event,EVENTTYPE eventType);
    }

    private interface Applicable<OPERAND,STATE extends Enum<STATE>,EVENTTYPE extends Enum<EVENTTYPE>,EVENT>{
        void apply(StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> factory);
    }

    private class ApplicableSingleOrMutipleArcTransition<OPERAND,STATE extends Enum<STATE>,EVENTTYPE extends Enum<EVENTTYPE>,EVENT>
            implements Applicable<OPERAND,STATE,EVENTTYPE,EVENT>{

        private final STATE preState;
        private final EVENTTYPE eventType;
        private final Transition<OPERAND,STATE,EVENTTYPE,EVENT> transition;

        public ApplicableSingleOrMutipleArcTransition(STATE preState,
                                                      EVENTTYPE eventType,
                                                      Transition<OPERAND,STATE,EVENTTYPE,EVENT> transition){
            this.preState = preState;
            this.eventType = eventType;
            this.transition = transition;
        }

        @Override
        public void apply(StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> factory) {
            //TODO
        }
    }

    private class TransitionListNode{
        private final ApplicableSingleOrMutipleArcTransition transition;
        private final TransitionListNode next;

        public TransitionListNode(ApplicableSingleOrMutipleArcTransition transition,TransitionListNode next){
            this.transition = transition;
            this.next = next;
        }

        public ApplicableSingleOrMutipleArcTransition transition(){
            return transition;
        }

        public TransitionListNode next(){
            return next;
        }


    }

    private class SingleArc<OPERAND,EVENT>
            implements Transition<OPERAND,STATE,EVENTTYPE,EVENT>{
        private final SingleArcTransition<OPERAND,EVENT> hook;
        private final STATE postState ;

        public SingleArc(STATE postState,SingleArcTransition<OPERAND,EVENT> hook){
            this.postState = postState;
            this.hook = hook;
        }

        @Override
        public STATE doTransition(OPERAND operand, STATE preState, EVENT event, EVENTTYPE eventType) {
            if (hook != null){
                hook.transition(operand, event);
            }
            return postState;
        }
    }

    private class MutipleArc<OPERAND,EVENT,STATE extends Enum<STATE>>
            implements Transition<OPERAND,STATE,EVENTTYPE,EVENT>{
        private final MultipleArcTransition<OPERAND,EVENT,STATE> hook;
        private final Set<STATE> postStates;

        public MutipleArc(Set<STATE> postStates,MultipleArcTransition<OPERAND,EVENT,STATE> hook){
            this.postStates = new HashSet<>(postStates);
            this.hook = hook;
        }
        @Override
        public STATE doTransition(OPERAND operand, STATE preState, EVENT event, EVENTTYPE eventType) {
            STATE state = hook.transition(operand,event);
            if (!postStates.contains(state)){
                throw new IllegalStateException(state.toString());
            }
            return state;
        }
    }

    private class InternalStateMachine implements StateMachine<STATE,EVENTTYPE,EVENT>{

        private final OPERAND operand;
        private STATE currentState;

        public InternalStateMachine(OPERAND operand,STATE initState){
            this.operand = operand;
            this.currentState = initState;
        }

        @Override
        public STATE getCurrentState() {
            return currentState;
        }

        @Override
        public STATE doTransition(EVENTTYPE eventType, EVENT event) {
            currentState =  StateMachineFactory.this.doTransition(operand,currentState,eventType,event);
            return currentState;
        }
    }

    public StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> addTransition(STATE preState,STATE postState,
                                             Set<EVENTTYPE> eventTypes,SingleArcTransition<OPERAND,EVENT> hook){
        StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> factory = this;
        for (EVENTTYPE eventType : eventTypes){
            factory = addTransition(preState,postState,eventType,hook);
        }
        return factory;
    }

    public StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> addTransition(STATE preState,STATE postState,
                                             EVENTTYPE eventType,SingleArcTransition<OPERAND,EVENT> hook){
        return new StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT>(
                this,
                new ApplicableSingleOrMutipleArcTransition<OPERAND,STATE,EVENTTYPE,EVENT>(
                        preState,
                        eventType,
                        new SingleArc<OPERAND, EVENT>(postState,hook)));
    }

    public StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> addTransition(STATE preState,Set<STATE> postStates,
                                             EVENTTYPE eventType,MultipleArcTransition<OPERAND,EVENT,STATE> hook){
        return new StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT>(
                this,
                new ApplicableSingleOrMutipleArcTransition<OPERAND,STATE,EVENTTYPE,EVENT>(
                        preState,
                        eventType,
                        new MutipleArc<OPERAND, EVENT, STATE>(postStates, hook)));
    }

    public StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> addTransition(STATE preState,Set<STATE> postStates,
                                             Set<EVENTTYPE> eventTypes,MultipleArcTransition<OPERAND,EVENT,STATE> hook){
        StateMachineFactory<OPERAND,STATE,EVENTTYPE,EVENT> factory = this;
        for (EVENTTYPE eventType : eventTypes){
            factory = addTransition(preState,postStates,eventType,hook);
        }
        return factory;
    }

    public void installTopology(){
        transitionTopology = new HashMap<>();
        Stack<TransitionListNode> stack = new Stack();
        for(TransitionListNode node = listNode; node != null; node = node.next){
            stack.push(node);
        }
        while (!stack.isEmpty()){
            stack.pop().transition.apply(this);
        }
    }

    private STATE doTransition(OPERAND operand,STATE preState,EVENTTYPE eventType,EVENT event){
        Map<EVENTTYPE, Transition<OPERAND,STATE,EVENTTYPE,EVENT>> map = transitionTopology.get(preState);
        if (map != null){
            Transition<OPERAND,STATE,EVENTTYPE,EVENT> transition = map.get(eventType);
            return transition.doTransition(operand, preState, event, eventType);
        }
        throw new IllegalStateException();
    }

    public StateMachine<STATE,EVENTTYPE,EVENT> make(OPERAND operand,STATE initState){
        return new InternalStateMachine(operand,initState);
    };
}
