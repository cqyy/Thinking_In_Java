package yuanye.Annotation;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Administrator on 14-3-10.
 */
public class UseCashTracker {
    public static void trackUsecase(Class<?> cl){
        for(Method method : cl.getDeclaredMethods()){
            UseCase uc = method.getAnnotation(UseCase.class);
            if(uc != null){
                System.out.println("id: " + uc.id() + " description: " + uc.description());
            }
        }
    }

    public static void main(String[] args){
//        trackUsecase(PassworUnti.class);
//        for(Method method : PassworUnti.class.getDeclaredMethods()){
//            System.out.println(method.isAnnotationPresent(UseCase.class));
//        }

        int[] arrays ={ 5,3,4,4,4,4,4,4};
        Object arraysObj = arrays;
        System.out.println(Arrays.asList(arrays));
    }
}
