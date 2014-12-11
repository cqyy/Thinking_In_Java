package yuanye.container;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StringFormat {

	public static void main(String[] args) {
		String lycis="Can't believe its over That you're leaving Weren't we meant to be?";
		Pattern pattern = Pattern.compile("[abcde]");
		Matcher matcher = pattern.matcher(lycis);
		StringBuffer sbuf = new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sbuf, matcher.group().toUpperCase());
		}
		matcher.appendTail(sbuf);
		
		System.out.print(sbuf.toString());
	}

}
