package cn.yuanye.container;

import java.awt.List;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/*
 * compare the differences between classes in Collections
 * */
public class ContainerMethodDifferences {
	public static Set<String> methodSet(Class<?> c){
		Set<String> result = new TreeSet<String>();
		
		for(Method method:c.getDeclaredMethods()){
			result.add(method.getName());
		}
		return result;
	}
	
	static Set<String> object = methodSet(Object.class);
	
	public static void differences(Class<?> superClass,Class<?> subClass){
		System.out.println(superClass.getName()+" extends "+subClass.getName()+" adds methods:");
		Set<String> result = new HashSet<String>(methodSet(superClass));
		result.removeAll(methodSet(subClass));
		result.removeAll(object);
		System.out.println(result);
	}
	
	public static void main(String[] args){
		
		ContainerMethodDifferences.differences(Set.class, Collections.class);
		ContainerMethodDifferences.differences(HashSet.class, Set.class);
		ContainerMethodDifferences.differences(ArrayList.class, List.class);
	}
	
}
