package yuanye.performancetest.nio;

public abstract class Test {
	protected String name;
	
	public Test(String _name){
		name = _name;
	}
	
	@Override
	public String toString(){
		return "test: "+name;
	}
	
	public String name(){
		return name;
	};
	
	public abstract void test(long count);
}
