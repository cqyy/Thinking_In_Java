package cn.yuanye.performancetest.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class IOCompareTest {
	static{
		long m = 1024*1024;
		Tester.times.addAll(Arrays.asList(16*m,32*m,64*m,128*m,256*m));
		
		Tester.tests.add(new Test("Stream write"){
			@Override
			public void test(long count) {
				
				try {
					String name = "temp"+count+".tmp";
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(name));
					for(int i = 0;i < count; i++){
						out.write(i);
					}
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
		
		Tester.tests.add(new Test("Channel write"){

			@Override
			public void test(long count) {
				String name = "temp"+count+".tmp";
				try {
					RandomAccessFile rac =  new RandomAccessFile(name,"rw");
					FileChannel fc =rac.getChannel();
					MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, count);

					for(int i = 0;i < count; i++){
						mbb.put((byte)i);
					}
				fc.close();
				rac.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
		
		Tester.tests.add(new Test("Stream read"){
			@Override
			public void test(long count) {
				String file = "temp"+count+".tmp";
				try {
					BufferedInputStream in = new BufferedInputStream(
							new FileInputStream(file));
					for(int i = 0;i < count ;i++){
						in.read();
					}
					in.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});

		Tester.tests.add(new Test("Channel read"){

			@Override
			public void test(long count) {
				String filename = "temp" + count + ".tmp";
				try {
					RandomAccessFile rac = new RandomAccessFile(filename, "rw");
					FileChannel fc = rac.getChannel();
					MappedByteBuffer mbb = fc.map(
							FileChannel.MapMode.READ_ONLY, 0, fc.size());
					while(mbb.hasRemaining()){
						mbb.get();
					}
					fc.close();
					rac.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
	}	
	public static void main(String[] args){
		Tester tester = new Tester();
		tester.timedTest();
	}
}
