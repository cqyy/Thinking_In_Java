package yuanye.container.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args){
		String test ="abcdefgABCDEFG";
		String regex = "a.*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(test);
		while(matcher.matches()){
			System.out.println(matcher.group());
		}
	}
}
