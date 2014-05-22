package yuanye.container.performancetest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;



public class ListPerformance {
	static Random rand = new Random();
	static int reps = 1000;
	static List<Test<List<Integer>>> tests = new ArrayList<Test<List<Integer>>>();
	static {
		tests.add(new Test<List<Integer>>("add"){
			
			@Override
			public int test(List<Integer> container, TestParam param) {
				for(int j =0;j<reps;j++){
					container.add(j);
				}
				return reps;
			}

			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param) {
				container.clear();
				return container;
			}
			
		});
		
		tests.add(new Test<List<Integer>>("get"){
			
			@Override
			public int test(List<Integer> container, TestParam param) {
				int listSize = param.size;
				for(int i = 0 ; i < reps ; i++){
					container.get(rand.nextInt(listSize));
				}
				return reps;
			}

			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
		
		tests.add(new Test<List<Integer>>("set"){
			
			@Override
			public int test(List<Integer> container, TestParam param) {
				int listSize = param.size;
				for(int i = 0 ; i < reps ; i++){
					container.set(rand.nextInt(listSize),32);
				}
				return reps;
			}

			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
		
		tests.add(new Test<List<Integer>>("iteradd"){

			@Override
			public int test(List<Integer> container, TestParam param) {
				ListIterator<Integer> iter = container.listIterator();
				for(int i=0;i<reps;i++){
					iter.add(32);
				}
				return reps;
			}
			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param ) {
				container.clear();
				return container;
			}
		});
		
		tests.add(new Test<List<Integer>>("insert"){

			@Override
			public int test(List<Integer> container, TestParam param) {
				int LOOPS = 100;
				for(int i = 0 ; i < LOOPS ; i++){
					container.add(3, 3);
				}
				return LOOPS;
			}

			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(3));
				return container;
			}});
		
		tests.add(new Test<List<Integer>>("remove"){

			@Override
			public int test(List<Integer> container, TestParam param) {
				int size =container.size();
				while(container.size()>3){
					container.remove(3);
				}
				return size;
			}

			@Override
			public List<Integer> prepara(List<Integer> container,TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
	}
	
	public static void main(String[] args){
		Tester.defaultParams = TestParam.array(10,5000,100,5000,1000,1000,10000,20);
		Tester.run(new ArrayList<Integer>(), tests);
		Tester.run(new LinkedList<Integer>(), tests);
		Tester.run(new Vector<Integer>(), tests);
	}
}
