package yuanye.reflection;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Kali on 14-5-22.
 */
public class SerializeDemo {

    public static Document serializeObject(Object obj) throws IllegalAccessException {
        return serializeHelper(obj, new Document(new Element("serialized")), new IdentityHashMap<Object, Integer>());
    }

    private static Document serializeHelper(Object source,
                                            Document target,
                                            IdentityHashMap<Object, Integer> table)
            throws IllegalAccessException {
        int id = table.size();
        Class<?> cl = source.getClass();
        table.put(source, id);
        Element oe = new Element("object");
        oe.setAttribute("class", cl.getName());
        oe.setAttribute("id", String.valueOf(id));
        target.getRootElement().addContent(oe);

        //deal normal object
        if (!cl.isArray()) {
            List<Field> fields = getInstanceFields(cl);

            for (Field field : fields) {
                if (!Modifier.isPublic(field.getModifiers()))
                    field.setAccessible(true);

                Element fe = new Element("field");
                fe.setAttribute("name", field.getName());
                fe.setAttribute("declaringClass", field.getDeclaringClass().getName());

                Object child = field.get(source);
                Class<?> childType = field.getType();
                if (Modifier.isTransient(child.getClass().getModifiers())) {
                    child = null;
                }
                fe.addContent(serializeVariable(childType, child, target, table));
                oe.addContent(fe);
            }
        } else {
            Class<?> componentType = cl.getComponentType();
            int length = Array.getLength(source);
            oe.setAttribute("length", String.valueOf(length));
            for (int i = 0; i < length; i++) {
                oe.addContent(serializeVariable(componentType, Array.get(source, i), target, table));
            }
        }
        return target;
    }

    private static Element serializeVariable(Class<?> cl, Object obj, Document target, IdentityHashMap<Object, Integer> table)
            throws IllegalAccessException {

        if (obj == null) {
            return new Element("null");
        }

        if (!cl.isPrimitive()) {
            Element reference = new Element("reference");
            if (table.containsKey(obj)) {
                int id = table.get(obj);
                reference.setText(String.valueOf(id));
            } else {
                int id = table.size();
                reference.setText(String.valueOf(id));
                serializeHelper(obj, target, table);
            }
            return reference;
        }
        Element value = new Element("value");
        value.setText(String.valueOf(obj));
        return value;
    }

    /**
     * Get all no static fields
     *
     * @param cl given class
     * @return list of fields
     */
    private static List<Field> getInstanceFields(Class<?> cl) {
        List<Field> result = new LinkedList<>();

        while (cl != null) {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    result.add(field);
                }
            }
            cl = cl.getSuperclass();
        }
        return result;
    }


    public static Object deserializeObject(Document document) throws ClassNotFoundException {
        List<Element> oEles = document.getRootElement().getChildren();
        Map<Integer, Object> table = new HashMap<>();
        createInstance(table, oEles);
        assignFieldValues(table, oEles);
        return table.get(0);
    }

    private static void createInstance(Map<Integer, Object> table, List<Element> oList)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        for (Element oEle : oList) {
            Class<?> cl = Class.forName(oEle.getAttributeValue("class"));
            Integer id = Integer.valueOf(oEle.getAttributeValue("id"));
            if (!cl.isArray()) {
                Constructor constructor = cl.getConstructor(null);
                Object obj = constructor.newInstance(null);
                table.put(id, obj);
            } else {
                int length = Integer.valueOf(oEle.getAttributeValue("length"));
                Object obj = Array.newInstance(cl, length);
                table.put(id, obj);
            }
        }
    }


    private static void assignFieldValues(Map<Integer, Object> table, List<Element> oList) throws NoSuchFieldException, IllegalAccessException {
        for (Element oEle : oList) {
            Object obj = table.get(Integer.valueOf(oEle.getAttributeValue("id")));
            Class<?> oc = obj.getClass();
            List<Element> fEles = oEle.getChildren();
            if (!oc.isArray()) {
                for (Element fEle : fEles) {
                    String fName = fEle.getAttributeValue("name");
                    Field field = oc.getField(fName);
                    if (!Modifier.isPublic(field.getModifiers()))
                        field.setAccessible(true);
                    field.set(obj, deserializeValue(table, oList, fEle));
                }
            }else{
                Class<?> componentType = oc.getComponentType();
                int length = Array.getLength(obj);
                for(int i = 0; i < length; i++){
                    Array.set(obj,i,deserializeValue(table,oList,oEle));
                }
            }
        }
    }

    private static Object deserializeValue(Map<Integer, Object> table, List<Element> oList, Element ele) {

    }

    public static void main(String[] args) throws IllegalAccessException, IOException {
        Zoo zoo = new Zoo("Chengdu National Zoo", "Chengdu");
        Panda panda1 = new Panda("Mei mei");
        Panda panda2 = new Panda("Zhuang zhuang");
        panda1.setClassification("Ailuropoda");
        panda1.setGender("male");
        panda1.setWeight(221);

        panda2.setClassification("Ailuropoda");
        panda2.setGender("female");
        panda2.setWeight(188);
        zoo.setPandas(new Panda[]{panda1, panda2});

        Document document = SerializeDemo.serializeObject(zoo);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
//        xmlOutputter.output(document,System.out);
        File file = new File("D://zoo.xml");
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        xmlOutputter.output(document, out);
        out.close();
    }
}

class Zoo {
    private final String name;
    private final String city;
    private Panda[] pandas;

    Zoo(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public void setPandas(Panda[] pandas) {
        this.pandas = pandas;
    }

}

class Panda {
    private final String name;
    private String gender;
    private int weight;
    private String classification;

    Panda(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}