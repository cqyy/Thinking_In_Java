package yuanye.datastructure;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import yuanye.datastructure.btree.BTreeLeaf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * BTreeLeaf Tester.
 *
 * @author <Authors name>
 * @since <pre>05/27/2014</pre>
 * @version 1.0
 */
public class BTreeLeafTest extends TestCase {


    public BTreeLeafTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(BTreeLeafTest.class);
    }

    public void testInsertKey(){
        try {
            Class<?> clazz = Class.forName("yuanye.datastructure.btree.BTreeLeaf");
            ParameterizedType genricType = (ParameterizedType) clazz.getGenericSuperclass();
            System.out.println(genricType.getTypeName());
            Constructor<?> constructor = clazz.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            BTreeLeaf leaf = (BTreeLeaf) constructor.newInstance(new Object[]{2});
            //Method method = clazz.getDeclaredMethod("insertKey",genricType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
