package yuanye.reflection.proxy.proxychain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Kali on 14-5-29.
 */
public abstract class InvocationHandlerBase implements InvocationHandler {

    protected Object nextTarget;
    protected Object realTarget;

    public InvocationHandlerBase(Object target) {
        this.nextTarget = target;
        this.realTarget = findRealTarget(nextTarget);
        if (realTarget == null) {
            throw new RuntimeException("can not find real target");
        }
    }

    public final Object realTarget() {
        return realTarget;
    }

    private static final Object findRealTarget(Object target) {
        if (!Proxy.isProxyClass(target.getClass())) {
            return target;
        }
        InvocationHandler ih = Proxy.getInvocationHandler(target);
        if (InvocationHandlerBase.class.isInstance(ih)) {
            return ((InvocationHandlerBase) target).realTarget();
        }
        try {
            Field field = findField("target", ih.getClass());
            if (Object.class.isAssignableFrom(field.getType())
                    && !field.getType().isArray()) {
                field.setAccessible(true);
                Object inerTarget = field.get(ih);
                return findRealTarget(inerTarget);
            }
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
        return null;
    }

    private static Field findField(String name, Class clazz) throws NoSuchFieldException {
        if (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(name);
                return field;
            } catch (NoSuchFieldException e) {
                return findField(name, clazz.getSuperclass());
            }
        }else {
            throw new NoSuchFieldException();
        }
    }
}
