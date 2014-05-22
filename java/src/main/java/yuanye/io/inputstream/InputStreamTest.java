package yuanye.io.inputstream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class InputStreamTest {

	public static void main(String[] args) throws IOException {
		Set<String> set = new HashSet<String>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add("124");
			add("233");
		}};
		System.out.println(set);
	}

}
