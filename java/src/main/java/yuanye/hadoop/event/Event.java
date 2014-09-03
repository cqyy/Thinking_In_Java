package yuanye.hadoop.event;

/**
 * Created by Kali on 14-9-3.
 */
public interface Event<TYPE extends Enum<TYPE>> {
    TYPE getType();

    String toString();
}
