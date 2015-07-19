package yuanye.reflection.proxy.stub;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public interface ReturnValueStrategy {
    Object getReturnValue(Proxy proxy,Method method,Object[] args,History history) throws WrappedException;

}
