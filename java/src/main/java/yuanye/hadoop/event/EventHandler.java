package yuanye.hadoop.event;

/**
 * Created by Kali on 14-9-3.
 */
public interface EventHandler<E extends Event> {

    public void handle(E event);

}
