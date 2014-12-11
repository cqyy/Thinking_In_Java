package yuanye.container.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileTest {
	public static void main(String[] args) throws IOException{
		File file = new File("F:/Music");
		String[] filenames = file.list(new FilenameFilter(){
			Pattern pattern = Pattern.compile(".*.lrc");
			@Override
			public boolean accept(File dir, String name) {
				return pattern.matcher(name).matches();
				
			}});
		for(String fn : filenames){
			System.out.println(fn);
		}
	}
}
