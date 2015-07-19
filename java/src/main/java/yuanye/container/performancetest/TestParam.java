package yuanye.container.performancetest;

public class TestParam {
	public final int size;
	public final int loops;

	public TestParam(int size,int loops){
		this.size = size;
		this.loops =  loops;
	}
	
	public static TestParam[] array(int...param){
		TestParam[] params = new TestParam[param.length/2];
		int n = 0;
		for(int i = 0; i<params.length;i++){
			params[i] = new TestParam(param[n++],param[n++]);
		}
		return params;
	}
}
