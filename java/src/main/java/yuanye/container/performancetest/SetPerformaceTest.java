package yuanye.container.performancetest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class SetPerformaceTest {
	 static List<Test<Set<Integer>>> tests = new ArrayList<Test<Set<Integer>>>(10);
	 static final int REPS = 1000;
	 static Random rand = new Random();
	 
	 static{
		 tests.add(new Test<Set<Integer>>("add"){

			@Override
			public int test(Set<Integer> container, TestParam param) {
				for(int i = 0; i< REPS; i++){
					container.add(55);
				}
				return REPS;
			}

			@Override
			public Set<Integer> prepara(Set<Integer> container, TestParam param) {
				container.clear();
				return container;
			}});
		 
		 tests.add(new Test<Set<Integer>>("remove"){

			@Override
			public int test(Set<Integer> container, TestParam param) {
				for(int i = 0; i < param.size ; i++){
					container.remove(i);
				}
				return param.size;
			}

			@Override
			public Set<Integer> prepara(Set<Integer> container, TestParam param) {	
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
		 
		 tests.add(new Test<Set<Integer>>("contains"){

			@Override
			public int test(Set<Integer> container, TestParam param) {
				int size = param.size;
				for(int i = 0;i < size ; i++){
					container.contains(i);
				}
				return size;
			}

			@Override
			public Set<Integer> prepara(Set<Integer> container, TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}
			 
		 });
		 
		 tests.add(new Test<Set<Integer>>("iterate"){

			@Override
			public int test(Set<Integer> container, TestParam param) {
				int LOOPS = 10;
				for(int i = 0; i <LOOPS ; i++){
					Iterator<Integer> iter = container.iterator();
					while(iter.hasNext()){
						iter.next();
					}
				}
				return LOOPS*param.size;
			}

			@Override
			public Set<Integer> prepara(Set<Integer> container, TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
		 
		tests.add(new Test<Set<Integer>>("clear"){

			@Override
			public int test(Set<Integer> container, TestParam param) {
				container.clear();
				return 1;
			}

			@Override
			public Set<Integer> prepara(Set<Integer> container, TestParam param) {
				container.clear();
				container.addAll(new CountingIntegerList(param.size));
				return container;
			}});
	 }
	 
	 
	 public static void main(String[] args){
		 Tester.defaultParams = TestParam.array(10,1000,100,1000,1000,1000,10000,100);
		 Tester.fieldWidth = 10;
		 Tester.run(new TreeSet<Integer>(), tests);
		 Tester.run(new HashSet<Integer>(), tests);
		 Tester.run(new LinkedHashSet<Integer>(),tests);
		 
	 }
}
