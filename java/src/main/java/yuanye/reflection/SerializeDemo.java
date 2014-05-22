package yuanye.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kali on 14-5-22.
 */
public class SerializeDemo {



    /**
     * Get all no static fields
     * @param cl given class
     * @return list of fields
     */
    private List<Field> getInstanceFields(Class<?> cl){
        List<Field> result = new LinkedList<>();

        while (cl != null){
            Field[] fields = cl.getDeclaredFields();
            for(Field field : fields){
                if (!Modifier.isStatic(field.getModifiers())){
                    result.add(field);
                }
            }
            cl = cl.getSuperclass();
        }
        return result;
    }


}
