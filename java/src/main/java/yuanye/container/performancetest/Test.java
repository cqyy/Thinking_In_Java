package yuanye.container.performancetest;

public abstract class Test<C> {
	final String name ;
	public Test(String name){
		this.name = name;
	}
	/**
	 * test action
	 * */
	public abstract int test(C container,TestParam param);
	
	
	/**
	 * action do before the test action,to do some preparation work
	 * */
	public abstract C prepara(C container,TestParam param);
}
