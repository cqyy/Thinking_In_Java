package yuanye.reflection;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
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

    private static Element serializeVariable(Class<?> cl,
                                             Object obj,
                                             Document target,
                                             IdentityHashMap<Object,Integer> table)
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


    public static Object deserializeObject(Document document)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {

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
            Object instance = null;
            if (!cl.isArray()) {
                Constructor constructor = cl.getConstructor(null);
                if (!Modifier.isPublic(constructor.getModifiers()))
                    constructor.setAccessible(true);

                instance = constructor.newInstance(null);
            } else {
                int length = Integer.valueOf(oEle.getAttributeValue("length"));
                Class<?> compoentType = cl.getComponentType();
                instance = Array.newInstance(compoentType, length);
            }
            table.put(id,instance);
        }
    }


    private static void assignFieldValues(Map<Integer, Object> table, List<Element> oList)
            throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        for(Element oEle : oList){
            Integer id = Integer.valueOf(oEle.getAttributeValue("id"));
            Object instance = table.get(id);
            Class<?> cls = Class.forName(oEle.getAttributeValue("class"));
            List<Element> children = oEle.getChildren();
            if (!cls.isArray()){
                for(Element child : children){
                    String declaringclass = child.getAttributeValue("declaringClass");
                    Class<?> childClass = Class.forName(declaringclass);
                    String fName = child.getAttributeValue("name");
                    Field field = childClass.getDeclaredField(fName);
                    if (!Modifier.isPublic(field.getModifiers()))
                        field.setAccessible(true);
                    Element ve = (Element) child.getChildren().get(0);
                    field.set(instance,deserializeValue(table,field.getType(),ve));
                }
            }else{
                Class<?> componentType = cls.getComponentType();
                int length = Array.getLength(instance);
                for(int i = 0; i < length; i++){
                    Array.set(instance,i,deserializeValue(table, componentType, children.get(i)));
                }
            }
        }
    }

    private static Object deserializeValue(Map<Integer, Object> table, Class<?> fieldType, Element ele) {
        String valType = ele.getName();
        if (valType.equals("null")){
            return null;
        }
        if (valType.equals("reference")){
            Integer id = Integer.valueOf(ele.getText());
            return table.get(id);
        }
        else{
            if (fieldType.equals(boolean.class)) {
                if (ele.getText().equals("true")) {
                    return Boolean.TRUE;
                }
                else {
                    return Boolean.FALSE;
                }
            }
            else if (fieldType.equals(byte.class)) {
                return Byte.valueOf(ele.getText());
            }
            else if (fieldType.equals(short.class)) {
                return Short.valueOf(ele.getText());
            }
            else if (fieldType.equals(int.class)) {
                return Integer.valueOf(ele.getText());
            }
            else if (fieldType.equals(long.class)) {
                return Long.valueOf(ele.getText());
            }
            else if (fieldType.equals(float.class)) {
                return Float.valueOf(ele.getText());
            }
            else if (fieldType.equals(double.class)) {
                return Double.valueOf(ele.getText());
            }
            else if (fieldType.equals(char.class)) {
                return new Character(ele.getText().charAt(0));
            }
            else {
                return ele.getText();
            }
        }
    }

    public static void main(String[] args)
            throws IllegalAccessException, IOException, ClassNotFoundException, NoSuchMethodException,
            NoSuchFieldException, InstantiationException, InvocationTargetException {
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
       // xmlOutputter.output(document,System.out);

        Zoo zoo2 = (Zoo) SerializeDemo.deserializeObject(document);
        System.out.println(zoo2.toString());

//        File file = new File("D://zoo.xml");
//        file.createNewFile();
//        FileOutputStream out = new FileOutputStream(file);
//        xmlOutputter.output(document, out);
//        out.close();
    }
}

class Zoo {
    private  String name;
    private  String city;
    private Panda[] pandas;

    public Zoo(){}

    Zoo(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public void setPandas(Panda[] pandas) {
        this.pandas = pandas;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Zoo ---  ")
                .append(" name: ").append(name)
                .append(" city: ").append(city)
                .append("[");
        for(Panda panda : pandas){
            sb.append(panda.toString()).append(" |");
        }
        sb.append("]");
        return sb.toString();
    }
}

class Panda {
    private  String name;
    private String gender;
    private int weight;
    private String classification;

    public Panda(){}

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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("panda ")
                .append(" name:").append(name)
                .append(" gender: ").append(gender)
                .append(" weight: ").append(weight)
                .append(" classification: ").append(classification);
        return sb.toString();
    }
}