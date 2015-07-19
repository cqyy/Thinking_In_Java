package yuanye.algorithm;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * FirstNoRepeatingChar Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 31, 2014</pre>
 */
public class FirstNoRepeatingCharTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: firstNoRepeatingCharWay1(String str)
     */
    @Test
    public void testFirstNoRepeatingCharWay1() throws Exception {
        assertEquals('b', FirstNoRepeatingChar.firstNoRepeatingCharWay1("abcdefghija"));
        assertEquals('h', FirstNoRepeatingChar.firstNoRepeatingCharWay1("hello"));
        assertEquals('J', FirstNoRepeatingChar.firstNoRepeatingCharWay1("Java"));
        assertEquals('i', FirstNoRepeatingChar.firstNoRepeatingCharWay1("simplest"));

    }

    /**
     * Method: firstNoRepeatingCharWay2(String str)
     */
    @Test
    public void testFirstNoRepeatingCharWay2() throws Exception {
        assertEquals('b', FirstNoRepeatingChar.firstNoRepeatingCharWay2("abcdefghija"));
        assertEquals('h', FirstNoRepeatingChar.firstNoRepeatingCharWay2("hello"));
        assertEquals('J', FirstNoRepeatingChar.firstNoRepeatingCharWay2("Java"));
        assertEquals('i', FirstNoRepeatingChar.firstNoRepeatingCharWay2("simplest"));

    }

    /**
     * Method: firstNoRepeatingCharWay3(String str)
     */
    @Test
    public void testFirstNoRepeatingCharWay3() throws Exception {
        assertEquals('b', FirstNoRepeatingChar.firstNoRepeatingCharWay3("abcdefghija"));
        assertEquals('h', FirstNoRepeatingChar.firstNoRepeatingCharWay3("hello"));
        assertEquals('J', FirstNoRepeatingChar.firstNoRepeatingCharWay3("Java"));
        assertEquals('i', FirstNoRepeatingChar.firstNoRepeatingCharWay3("simplest"));

    }


} 
