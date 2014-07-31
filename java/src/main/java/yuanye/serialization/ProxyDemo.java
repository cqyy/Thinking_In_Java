package yuanye.serialization;

import java.io.*;

/**
 * Created by Kali on 2014/7/19.
 */
public class ProxyDemo {

    private static class Person implements Serializable{
        private final String name;
        private final int age;

        public Person(String name,int age){
            this.name = name;
            this.age = age;
        }

        private static class PersonProxy implements Serializable{
            private static final long serialVersionUID = 6679468263480124365L;
            private String name;
            private int age;

            public PersonProxy(Person person){
                this.name = person.name;
                this.age = person.age;
            }

            private Object readResolve() throws ObjectStreamException{
                return new Person(name,age);
            }
        }

        private Object writeReplace() throws ObjectStreamException {
            return new PersonProxy(this);
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
        Person person = new Person("Kitty",18);
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
