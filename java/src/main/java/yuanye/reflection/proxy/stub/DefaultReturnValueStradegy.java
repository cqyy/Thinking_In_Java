package yuanye.reflection.proxy.stub;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public class DefaultReturnValueStradegy implements ReturnValueStrategy {
    @Override
    public Object getReturnValue(Proxy proxy, Method method, Object[] args, History history) throws WrappedException {
        Class<?> type = method.getReturnType();
        if (!type.isPrimitive()){
            try {
                return type.newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        //primitive type,return wrapper object

        else if (type == int.class){
            return Integer.valueOf(0);
        }
        else if (type == void.class){
            return null;
        }
        else if (type == boolean.class){
            return Boolean.FALSE;
        }
        else if (type == byte.class){
            return Byte.valueOf((byte)0);
        }
        else if (type == char.class){
            return Character.valueOf('a');
        }
        else if (type == long.class){
            return Long.valueOf(0);
        }
        else if (type == float.class){
            return Float.valueOf(0);
        }
        else if (type == short.class){
            return Short.valueOf((short)0);
        }
        else if (type == double.class){
            return Double.valueOf(0);
        }
        throw new RuntimeException("Unknown type : " + type.getName());
    }
}
