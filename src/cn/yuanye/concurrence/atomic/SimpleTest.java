package cn.yuanye.concurrence.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleTest {

	public static void main(String[] args) {
		AtomicInteger ai = new AtomicInteger(0);
		System.out.println(ai.getAndIncrement());
		System.out.println(ai);
		System.out.println(ai.incrementAndGet());
	}

}
