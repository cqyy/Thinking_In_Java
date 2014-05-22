package yuanye.container.performancetest;

import java.util.List;

public class Tester<C> {
	public static TestParam[] defaultParams = TestParam.array(50,1000,500,1000,5000,1000,50000,100);
	C container;
	private List<Test<C>> tests;
	private String headline;
	private TestParam[] params = defaultParams;
	public static int fieldWidth = 8;
	public static int sizeWidth = 5;
	private static String sizeField = "%"+sizeWidth+"s";
	
	private static String stringField(){
		return "%"+fieldWidth+"s";
	}
	
	private static String numberFiled(){
		return "%"+fieldWidth+"d";
	}
	
	protected C initialize(int size){return container;}
	
	public Tester(C container , List<Test<C>> tests){
		this.tests = tests;
		this.container = container;
		this.headline = container.getClass().getSimpleName();
	}
	
	public Tester(C container,List<Test<C>> tests,TestParam[] params){
		this(container,tests);
		this.params = params;
	}
	
	public void setHeadline(String headline){
		this.headline = headline;
	}
	
	public static<C> void run(C container,List<Test<C>> tests){
		new Tester<C>(container,tests).timedTest();
	}
	
	public static<C> void run(C container,List<Test<C>> tests,TestParam[] params){
		new Tester<C>(container,tests,params).timedTest();
	}
	
	private void displayHeader(){
		int width = fieldWidth * tests.size() + sizeWidth;
		int dashLength = width - headline.length() -1;
		StringBuilder sb = new StringBuilder(width);
		for(int i=0; i<dashLength/2;i++){
			sb.append("-");
		}
		sb.append(" ");
		sb.append(headline);
		sb.append(" ");
		for(int i = 0; i<dashLength/2;i++){
			sb.append("-");
		}
		System.out.println(sb);
		System.out.format(sizeField, "size");
		for(Test<C> test : tests){
			System.out.format(stringField(), test.name);
		}
		System.out.println();
	}
	
	public void timedTest(){
		displayHeader();
		for(TestParam param : params){
			System.out.format(sizeField, param.size);
			for(Test<C> test : tests){
				
				C kontainer = initialize(param.size);
				int loops = param.loops;
				long duration = 0;
				int reps = 0;
			
				for(int i =0 ;i<loops;i++){
					kontainer = test.prepara(kontainer,param);
					long start = System.nanoTime();
					reps += test.test(kontainer, param);
					duration += System.nanoTime() -start;
				}

				long timePerRep = duration /reps;
				System.out.format(numberFiled(),timePerRep);
			}
			System.out.println();
		}
	}

}
