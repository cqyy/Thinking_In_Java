package yuanye.reflection;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kali on 14-5-22.
 */
public class MethodExample {

    /**
     * Print out all methods of given class
     * @param cl class object
     */
    public static void printMethods(Class<?> cl){
        Set<Method> methods = new HashSet<>();
        StringBuffer sb = new StringBuffer();
        String newLine = System.getProperty("line.separator");
        //get all declared methods
        for(Method mt : cl.getDeclaredMethods()){
            methods.add(mt);
        }

        //get all public methods including those inherit from parent
        for (Method mt : cl.getMethods()){
            methods.add(mt);
        }

        //format methods to string
        for(Method mt : methods){
            sb.append(modifier(mt.getModifiers()))
                    .append(" ")
                    .append(mt.getReturnType().getSimpleName())
                    .append(" ")
                    .append(mt.getName())
                    .append(" ( ")
                    .append(parameters(mt))
                    .append(" )")
                    .append(newLine);
        }

        System.out.println(sb);
    }

    private static String parameters(Method mt){
        Parameter[] parameters = mt.getParameters();
        String result = "";
        for(Parameter p : parameters){
            result += p.getType().getSimpleName();
            result += " " + p.getName();
            result += " , ";
        }

        //remove last comma symbol
        if (result.length() > 0){
            result = result.substring(0,result.length() -2);
        }
        return result;
    }

    private static String modifier(int m){
        return Modifier.toString(m);
    }

    public static void main(String[] args) {
        printMethods(ConcurrentHashMap.class);
    }
}
