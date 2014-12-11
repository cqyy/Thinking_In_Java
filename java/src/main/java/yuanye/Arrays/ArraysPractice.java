package yuanye.Arrays;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArraysPractice {

	public static void main(String[] args) {
		String[] strs = ("The furthest distance in the world "
				+"Is not between life and death "
				+ "But when I stand in front of you "
				+ "Yet you don't know that I love you").split("\\s");
		/*create a list from arrays*/
		List<String> list = Arrays.asList(strs);
		System.out.println(list);
		/*arrays toString*/
		System.out.println(Arrays.toString(strs));
		
		/*array copy*/
		String[] subStr = Arrays.copyOf(strs, 5);
		System.out.println(Arrays.toString(subStr));
		
		/*array copy rang,the subString length == to - from*/
		String[] subStr2 = Arrays.copyOfRange(strs, 1,2);
		System.out.println(Arrays.toString(subStr2));
		
		/*sort the array*/
		Arrays.sort(strs);
		System.out.println(Arrays.toString(strs));
		
		/*sort the array by length*/
		Arrays.sort(strs, new Comparator<String>(){
			@Override
			public int compare(String str1, String str2) {
				return str1.length()-str2.length();
			}	
		});
		System.out.println(Arrays.toString(strs));
		
		
	}

}
