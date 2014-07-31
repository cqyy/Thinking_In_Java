package yuanye.serialization;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kali on 2014/7/19.
 */
public class CustomDemo {

    private static class Person implements Serializable{
        private static final long serialVersionUID = 3308780890814766690L;
        private String name;

        public Person(String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Persons implements Serializable{
        private static final long serialVersionUID = 3632886818488066643L;
        private transient List<Person> personList = new LinkedList<Person>();
        private transient int size = 0;

        public void addPerson(Person person){
            personList.add(person);
            size++;
        }

        private void writeObject(java.io.ObjectOutputStream out)
                throws IOException {
            out.writeObject(size);
            for (Person p : personList){
                out.writeObject(p);
            }
        }
        private void readObject(java.io.ObjectInputStream in)
                throws IOException, ClassNotFoundException{
            size = (int) in.readObject();
            personList = new LinkedList<>();
            for (int i = 0; i < size; i++){
                addPerson((Person)in.readObject());
            }
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (Person person : personList){
                sb.append(person);
                sb.append(",");
            }
            if (sb.length() > 1){
                sb.deleteCharAt(sb.length() -1);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Persons persons = new Persons();
        for (int i = 0; i < 10; i++){
            persons.addPerson(new Person("person" + i));
        }

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
        System.out.println("Before serialization:" + persons);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(persons);
        oos.flush();
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Persons dps = (Persons)ois.readObject();
        System.out.println("After deserialization: " + dps);
        ois.close();
    }
}
