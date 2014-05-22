package yuanye.performancetest.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZIPCompress {
	
	private OutputStream out = null;
	private CheckedOutputStream cos = null;
	private ZipOutputStream zos = null;
	private BufferedOutputStream bos = null;
	private String absoluteBasePath = null;
	
	
	public void compress(String filename,String outname) throws FileNotFoundException {
		File file = new File(filename);
		if (!file.exists()) {
			throw new FileNotFoundException(filename);
		}
		
		absoluteBasePath = ( file.getParent() == null )?"":file.getParent()+"\\";
		out = new FileOutputStream(outname + ".zip");
		cos = new CheckedOutputStream(out, new Adler32());
		zos = new ZipOutputStream(cos);
		bos = new BufferedOutputStream(zos);
		recompress("",file);
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recompress(String p,File file) {
		String path = p + file.getName();
		
		try {
			if (file.isDirectory()) {
				path += "\\";
				zos.putNextEntry(new ZipEntry(path));
				String[] filenames = file.list();
				for(String filename : filenames){
					recompress(path,new File(absoluteBasePath + path + "\\" + filename));
				}
			}else{
				zos.putNextEntry(new ZipEntry(path));
				BufferedInputStream in = new BufferedInputStream(
						 new FileInputStream(absoluteBasePath + path));
				int c;
				while((c = in.read()) != -1){
					bos.write(c);
				}
				in.close();
				bos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void decompress(String zipfile,String outpath){
		
	};
	public static void main(String[] args) throws IOException {
		ZIPCompress zip = new ZIPCompress();
		zip.compress("test.ppt","test");
		
//		CheckedOutputStream cos = new CheckedOutputStream(
//				new FileOutputStream("test.ppt.zip"),new Adler32());
//		ZipOutputStream zos = new ZipOutputStream(cos);
//		BufferedOutputStream bos = new BufferedOutputStream(zos);
//		
//		BufferedInputStream bis = new BufferedInputStream(
//				new FileInputStream("test.ppt"));
//		zos.putNextEntry(new ZipEntry("test.ppt"));
//		
//		int c;
//		while((c = bis.read()) != -1){
//			bos.write(c);
//		}
//		bos.flush();
//		bis.close();
//		bos.close();
		
		
		
//		InputStream in = new FileInputStream("test.ppt.zip");
//		CheckedInputStream cis = new CheckedInputStream(in, new CRC32());
//		ZipInputStream zis = new ZipInputStream(cis);
//		BufferedInputStream bis2 = new BufferedInputStream(zis);
//		//ZipEntry e = zis.getNextEntry();
//		
//		BufferedOutputStream bos2 = new BufferedOutputStream(
//				new FileOutputStream("test2.ppt"));
//		int b;
//		
//		while((b=bis2.read()) != -1){
//			bos2.write(b);
//		}
//		bos2.flush();
//		bos2.close();
//		bis2.close();
		
		
//		File file = new File("E:\\");
//		System.out.println(file.getParent());
	}
}
