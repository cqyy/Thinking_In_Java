package yuanye.hadoop;

import yuanye.hadoop.fsm.SingleArcTransition;
import yuanye.hadoop.fsm.StateMachine;
import yuanye.hadoop.fsm.StateMachineFactory;

/**
 * Created by Kali on 9/17/14.
 */
public class FsmTest {

    private static enum JobState {
        JOB_NEW,
        JOB_STARTED,
        JOB_RUNNING,
        JOB_COMPLITTED;
    }

    private static enum JobEventType {
        START,
        RUN,
        COMPLETE;
    }

    private static class Job {
        private final String name;

        public Job(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }

    private static class JobEvent {
    }

    private static class StartJobTransition implements SingleArcTransition<Job, JobEvent> {

        @Override
        public void transition(Job job, JobEvent jobEvent) {
            System.out.println("Start job: " + job.name());
        }
    }

    private static class RunJobTransition implements SingleArcTransition<Job, JobEvent> {

        @Override
        public void transition(Job job, JobEvent jobEvent) {
            System.out.println("Run job:" + job.name());
        }
    }

    private static class CompletingTransition implements SingleArcTransition<Job, JobEvent> {

        @Override
        public void transition(Job job, JobEvent jobEvent) {
            System.out.println("Complete job:" + job.name());
        }
    }

    public static void main(String[] args) {
        StateMachineFactory<Job, JobState, JobEventType, JobEvent> stateMachineFactory =
                new StateMachineFactory<Job, JobState, JobEventType, JobEvent>(JobState.JOB_NEW)
                        //from JOB_NEW
                        .addTransition(JobState.JOB_NEW, JobState.JOB_STARTED,
                                JobEventType.START, new StartJobTransition())
                        //from JOB_STARTED
                        .addTransition(JobState.JOB_STARTED, JobState.JOB_RUNNING,
                                JobEventType.RUN, new RunJobTransition())
                        //from JOB_RUNNING
                        .addTransition(JobState.JOB_RUNNING, JobState.JOB_COMPLITTED,                                JobEventType.COMPLETE, new CompletingTransition())
                        .installTopology();
        Job job = new Job("Test Job");
        StateMachine<JobState, JobEventType, JobEvent> stateMachine =
                stateMachineFactory.make(job, JobState.JOB_NEW);

        stateMachine.doTransition(JobEventType.START, new JobEvent());
        stateMachine.doTransition(JobEventType.RUN, new JobEvent());
        stateMachine.doTransition(JobEventType.COMPLETE, new JobEvent());

        System.out.println("Final state:" + stateMachine.getCurrentState());
    }
}
