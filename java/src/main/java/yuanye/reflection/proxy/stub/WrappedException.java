package yuanye.reflection.proxy.stub;

/**
 * Created by Kali on 14-5-29.
 */
public class WrappedException extends Exception {
    public WrappedException(Throwable throwable){
        super(throwable);
    }
}
