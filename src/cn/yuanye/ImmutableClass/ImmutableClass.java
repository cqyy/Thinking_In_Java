package cn.yuanye.ImmutableClass;

import java.util.HashMap;
/**
 * using builder to build an immutable class object
 * */
public class ImmutableClass {
	//required fields
	private int id;
	private String name;
	
	//optional fields
	private HashMap<String,String> properties;
	private String company;
	
	public int id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,String> properties(){
		//clone an object of properties ,avoid change it 
		return (HashMap<String,String>)properties.clone();
	}
	
	public String company(){
		return company;
	}

	public static class ImmutableClassBuilder{
		private int id;
		private String name;
		private HashMap<String,String> pro;
		private String company;
		
		public ImmutableClassBuilder(int id,String name){
			this.id = id;
			this.name = name;
		}
		
		public void setProperties(HashMap<String,String> pro){
			this.pro = pro;
		}
		
		public void setCompany(String company){
			this.company = company;
		}
		
		public ImmutableClass build(){
			ImmutableClass ic = new ImmutableClass();
			ic.id = id;
			ic.name = name;
			ic.properties = pro;
			ic.company = company;
			return ic;
		}
		
	}

	public static void main(String[] args){
		
		HashMap<String,String> pro = new HashMap<String,String>();
		pro.put("uestc", "Chengdu");
		pro.put("upc", "Qingdao");
		ImmutableClassBuilder icb = new ImmutableClass.ImmutableClassBuilder(1,"name");
		icb.setCompany("company");
		icb.setProperties(pro);
		
		ImmutableClass ic = icb.build();
		System.out.println("before edit: "+ic.properties);
		pro = ic.properties();
		pro.put("CQU", "Chongqing");
		System.out.println("edit properties "+pro);
		System.out.println("after edit: "+ic.properties);
		
	}
}
