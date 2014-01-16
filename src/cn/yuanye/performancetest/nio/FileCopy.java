package cn.yuanye.performancetest.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {
	public static void main(String[] args) throws IOException{
		String infile = "test.ppt";
		String outfile = "test2.ppt";
		
		BufferedInputStream in = new BufferedInputStream(
				new FileInputStream(infile));
		
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(outfile));
		
		int c;
		while((c = in.read()) != -1){
			out.write(c);
		}
		
		out.close();
		in.close();
	}
}
