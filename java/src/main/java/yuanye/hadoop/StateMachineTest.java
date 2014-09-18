package yuanye.hadoop;

/**
 * Created by Administrator on 2014/9/17.
 */
public class StateMachineTest {

    private enum JobState{
        JOB_NEW,
        JOB_STARTED,
        JOB_RUNNING,
        JOB_KILLED,
        JOB_FAILED,
        JOB_COMPLICATED;
    }


    public static void main(String[] args) {
        //StateMachineFactory
    }
}
