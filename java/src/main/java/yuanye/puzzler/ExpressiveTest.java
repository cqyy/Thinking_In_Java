package yuanye.puzzler;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class ExpressiveTest extends TestCase {


    @Test
    public void isOddTest(){
        List<Integer> integers = Arrays.asList(-1,1,2,3,4);
        List<Boolean> results = Arrays.asList(Boolean.TRUE,Boolean.TRUE,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE);
        for (int i = 0; i < integers.size(); i++){
            if (!results.get(i).equals(Expressive.isOdd(integers.get(i)))){
                System.out.println(integers.get(i));
                fail();
            }
        }
    }

}