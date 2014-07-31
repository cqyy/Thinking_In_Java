package yuanye.serialization;

import java.io.*;

/**
 * Created by Kali on 2014/7/19.
 */
public class BasicDemo {

    private static class Person implements Serializable ,ObjectInputValidation {

        private static final long serialVersionUID = 9175036933185692367L;

        private String name;

        public Person(String name){
            this.name = name;
        }
        @Override
        public String toString() {
            return "Person " + name;
        }

        @Override
        public void validateObject() throws InvalidObjectException {
            System.out.println("verify");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file = new File("D:\\","output");
        if (!file.exists()){
            if (!file.getParentFile().exists()){
                if (!file.getParentFile().mkdirs()){
                    throw new IOException("Could not create folder " + file.getParent());
                }
            }
            if (!file.createNewFile()){
                throw new IOException("Could not create file " + file);
            }
        }
        Person person = new Person("Kitty");
        System.out.println("Before serialization:" + person);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(person);
        oos.flush();
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Person dp = (Person)ois.readObject();
        ois.close();
        System.out.println("After deserialization: " + dp);
    }
}
