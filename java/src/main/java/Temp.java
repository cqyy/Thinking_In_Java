/**
 * Created by Kali on 14-5-13.
 */
public class Temp {

    private static class Class1{
        private final String str;

        private Class1(String str) {
            this.str = str;
        }
    }

    public void printName(){
        System.out.println(this.getClass().getName());
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {


    }
}

