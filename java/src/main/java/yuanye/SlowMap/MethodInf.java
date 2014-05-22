package yuanye.SlowMap;

import java.lang.reflect.Method;

//store information
public class MethodInf {
	protected String reType;
	protected String name;
	protected String[] params;
	protected String[] exceptions;
	protected boolean isVarArgs;
	protected boolean isSyn;
	
	public MethodInf(Method mt){
		reType = mt.getReturnType().getSimpleName();
		name = mt.getName();
		Class<?>[] ps = mt.getParameterTypes();
		params = new String[ps.length];
		for(int i = 0;i < params.length; i++){
			params[i] = ps[i].getSimpleName();
		}
	}
	
	public String returnType(){
		return this.reType;
	}
}
