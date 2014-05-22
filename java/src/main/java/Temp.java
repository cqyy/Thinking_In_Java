import org.jdom.Document;

/**
 * Created by Kali on 14-5-13.
 */
public class Temp {

    private static class c1 {

        public String str = "normal string";
        private String str1 = "private string";
        private final String str2 = "private final string";

        public void printName(){
            System.out.println(this.getClass().getName());
        }

        @Override
        public String toString() {
            return str + " " + str1  + " " + str2;
        }
    }


    public static void main(String[] args) {
        Document doc = new Document();
    }
}

