package yuanye.container.performancetest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class MapPerformanceTest {
	static List<Test<Map<Integer,Integer>>> tests = new ArrayList<Test<Map<Integer,Integer>>>(10);
	
	
	static{
		tests.add(new Test<Map<Integer,Integer>>("put"){

			@Override
			public int test(Map<Integer, Integer> container, TestParam param) {
				int size =  param.size;
				for(int i = 0;i < size ; i++){
					container.put(i, i);
				}
				return size;
			}

			@Override
			public Map<Integer, Integer> prepara(
					Map<Integer, Integer> container, TestParam param) {
				container.clear();
				return container;
			}});
		
		tests.add(new Test<Map<Integer,Integer>>("get"){

			@Override
			public int test(Map<Integer, Integer> container, TestParam param) {
				for(int i = 0;i < param.size; i++){
					container.get(i);
				}
				return param.size;
			}

			@Override
			public Map<Integer, Integer> prepara(
					Map<Integer, Integer> container, TestParam param) {
				container.clear();
				for(int i = 0;i<param.size;i++){
					container.put(i, i);
				}
				return container;
			}});
		
		tests.add(new Test<Map<Integer,Integer>>("containe"){

			@Override
			public int test(Map<Integer, Integer> container, TestParam param) {
					for(int j = 0; j < param.size; j++){
						container.containsKey(j);
					}
				return  param.size;
			}

			@Override
			public Map<Integer, Integer> prepara(
					Map<Integer, Integer> container, TestParam param) {
				container.clear();
				for(int i = 0;i<param.size;i++){
					container.put(i, i);
				}
				return container;
			}});

		tests.add(new Test<Map<Integer,Integer>>("iterate"){

			@Override
			public int test(Map<Integer, Integer> container, TestParam param) {
				int loops = 100;
				for(int i =0;i<loops;i++){
					Iterator<Entry<Integer, Integer>> it = container.entrySet().iterator();
					while(it.hasNext()){
						it.next();
					}
				}
				return loops*param.size;
			}

			@Override
			public Map<Integer, Integer> prepara(
					Map<Integer, Integer> container, TestParam param) {
				container.clear();
				for(int i = 0;i<param.size;i++){
					container.put(i, i);
				}
				return container;
			}});
	}
	
	public static void main(String[] args){
		Tester.defaultParams = TestParam.array(10,1000,100,1000,1000,1000,10000,100);
		Tester.fieldWidth = 10;
		Tester.run(new TreeMap<Integer,Integer>(), tests);
		Tester.run(new HashMap<Integer,Integer>(),tests);
		Tester.run(new LinkedHashMap<Integer,Integer>(), tests);
		Tester.run(new IdentityHashMap<Integer,Integer>(), tests);
		Tester.run(new WeakHashMap<Integer,Integer>(), tests);
		Tester.run(new Hashtable<Integer,Integer>(),tests);
	}
	
}
