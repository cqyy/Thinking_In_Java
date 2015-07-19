package yuanye.hadoop.event;

/**
 * Created by Kali on 14-9-3.
 */
public class AbstractEvent<TYPE extends Enum<TYPE>> implements Event<TYPE> {

    private final TYPE type;

    public AbstractEvent(TYPE type){
        this.type = type;
    }

    @Override
    public TYPE getType() {
        return type;
    }
}
