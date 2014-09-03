package yuanye.hadoop;

import yuanye.hadoop.event.AbstractEvent;
import yuanye.hadoop.event.AsyncDispatcher;
import yuanye.hadoop.event.Dispatcher;
import yuanye.hadoop.event.EventHandler;
import yuanye.hadoop.service.CompositeServer;

/**
 * Created by Kali on 14-9-3.
 */
public class EventDrivenTest {

    public static enum TaskEventType {
        T_KILL,
        T_SCHEDULE;
    }

    public static enum JobEventType {
        JOB_KILL,
        JOB_INIT,
        JOB_START;
    }

    public static class TaskEvent extends AbstractEvent<TaskEventType> {

        private final String taskID;

        public TaskEvent(String taskID, TaskEventType type) {
            super(type);
            this.taskID = taskID;
        }

        public String getTaskID() {
            return taskID;
        }

        @Override
        public String toString() {
            return "TaskEvent " + taskID + " type " + getType();
        }
    }

    public static class JobEvent extends AbstractEvent<JobEventType> {

        private final String jobID;

        public JobEvent(String jobID, JobEventType jobEventType) {
            super(jobEventType);
            this.jobID = jobID;
        }

        public String getJobID() {
            return jobID;
        }

        @Override
        public String toString() {
            return "JobEvent " + jobID +" type " + getType();
        }
    }

    public static class SimpleMRAppMaster extends CompositeServer {

        private Dispatcher dispatcher;
        private String jobID;
        private int taskNummber;
        private String[] taskIDs;

        public SimpleMRAppMaster(String jobID, int taskNummber) {
            this.jobID = jobID;
            this.taskNummber = taskNummber;

            taskIDs = new String[taskNummber];
            for (int i = 0; i < taskNummber; i++) {
                taskIDs[i] = jobID + "_task_" + i;
            }
        }

        @Override
        public void init() {
            super.init();
            dispatcher = new AsyncDispatcher(5);
            dispatcher.init();
            dispatcher.registerEventHandler(JobEventType.class,new JobEventHandler());
            dispatcher.registerEventHandler(TaskEventType.class,new TaskEventHandler());
            addService(dispatcher);
        }

        public  class TaskEventHandler implements EventHandler<TaskEvent> {
            @Override
            public void handle(TaskEvent event) {
                System.out.println("Handling event " + event);
                if (event.getType() == TaskEventType.T_KILL) {
                    System.out.println("Received T_KILL event of task " + event.getTaskID());
                }else if (event.getType() == TaskEventType.T_SCHEDULE){
                    System.out.println("Received T_SCHEDULE event of task " + event.getTaskID());
                }
            }
        }

        public Dispatcher getDispatcher(){
            return dispatcher;
        }

        public class JobEventHandler implements EventHandler<JobEvent>{
            @Override
            public void handle(JobEvent event) {
                if (event.getType() == JobEventType.JOB_KILL){
                    System.out.println("Received JOB_KILL event of job "  + event.getJobID());
                    System.out.println("Killing all tasks");
                    for(int i = 0; i < taskNummber; i++){
                        dispatcher.getEventHandler().handle(new TaskEvent(taskIDs[i], TaskEventType.T_KILL));
                    }
                }else if (event.getType() == JobEventType.JOB_INIT){
                    System.out.println("Received JOB_INIT event,scheduling tasks");
                    for(int i = 0; i < taskIDs.length; i++){
                        dispatcher.getEventHandler().handle(new TaskEvent(taskIDs[i], TaskEventType.T_SCHEDULE));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String jobID = "job_2014903_1";
        SimpleMRAppMaster mrAppMaster = new SimpleMRAppMaster(jobID,5);
        mrAppMaster.init();
        mrAppMaster.start();
        mrAppMaster.getDispatcher().getEventHandler().handle(new JobEvent(jobID, JobEventType.JOB_KILL));
        mrAppMaster.getDispatcher().getEventHandler().handle(new JobEvent(jobID, JobEventType.JOB_INIT));
        //mrAppMaster.stop();
    }
}
