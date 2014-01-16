package cn.yuanye.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyHandler implements InvocationHandler {
	
	private Object proxied;
	
	public DynamicProxyHandler(Object obj){
		this.proxied = obj;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("Dynamic Proxy Method Invoke:"+method.getName());
		return method.invoke(proxied, args);
	}
	
	public static void main(String[] args){
		InterfaceImplementation im = new InterfaceImplementation();
		Interface proxy = (Interface)Proxy.newProxyInstance(
				Interface.class.getClassLoader(), 
				new Class[]{Interface.class},
				new DynamicProxyHandler(im));
		
		proxy.doSomething();
		proxy.doSomething("str");
	}
}
