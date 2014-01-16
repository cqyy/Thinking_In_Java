package cn.yuanye.container;

public class InterfaceImplementation implements Interface {

	@Override
	public void doSomething() {
		System.out.println("doSomething()");
		
	}

	@Override
	public void doSomething(String str) {
		System.out.format("doSomething(%s)", str);
		
	}

}
