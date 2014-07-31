package yuanye.serialization;

import java.io.*;

/**
 * Created by Kali on 2014/7/19.
 */
public class SercureDemo {

    private static class Person implements Serializable{
        private String name;
        private int age;

        public Person(String name,int age){
            this.name = name;
            this.age = age;
        }

        private void encrypt(){
            //encrypt age
            age = age << 2;
        }

        private void deocde(){
            //decode age
            age = age >>2;
        }

        private void writeObject(java.io.ObjectOutputStream out)
                throws IOException {
            encrypt();
            out.defaultWriteObject();
        }

        private void readObject(java.io.ObjectInputStream in)
                throws IOException, ClassNotFoundException{
             in.defaultReadObject();
             deocde();
        };

        @Override
        public String toString() {
            return String.format("Person [name: %s, age: %d]",name,age);
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
