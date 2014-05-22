package yuanye.HashCode;

import java.util.Arrays;

public class CountedString {
	private String s;
	private int id = 0;
	private char c;
	
	public CountedString(String str,int i,char ch){
		s = str;
		id = i;
		c = ch;
	}
	
	@Override
	public String toString(){
		return s+"-"+id+"-"+c +" hashCode:"+hashCode();
	}
	
	@Override
	public int hashCode(){
		int code = 17;
		code = 37*code + s.hashCode();
		code = 37*code + id;
		code = 37*code +c;
		return code;
	}
	
	
	public static void main(String[] args){
		CountedString[] cs = new CountedString[5];
		for(int i=0;i<cs.length;i++){
			cs[i] = new CountedString("str",i,'a');
		}
		System.out.print(Arrays.toString(cs));
	}
}
