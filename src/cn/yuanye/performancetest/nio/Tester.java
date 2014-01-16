package cn.yuanye.performancetest.nio;

import java.util.ArrayList;
import java.util.List;

public class Tester {
	public static List<Test> tests = new ArrayList<Test>();
	public static List<Long> times = new ArrayList<Long>();
	
	public void timedTest(){
		
		for (Test test : tests) {
			System.out.println("--------" + test.name + "--------");
			for (long time : times) {
				long start = System.nanoTime();
				test.test(time);
				long dura =  System.nanoTime() - start;
				System.out.format("size(M):%6d   totalTime:%14d,   speed(ns/byte)%8.2f\n",
						time/(1024*1024),dura,(float)dura/time);
			}
		}
	}
}
