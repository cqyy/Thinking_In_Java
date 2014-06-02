package yuanye.mockito;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
/**
 * Created by Kali on 2014/6/2.
 * Demo of ways to using Mockito,one of unit test tools.
 */
public class MockitoDemo {
    public static void main(String[] args) {
        invocationTimes();
        //argumentMatcher();
        //stubTest();
        //verifyBehaviour();
    }

    static void verifyBehaviour(){
        //mock creation
        List<String> mockedList = mock(List.class);

        //using mock object
        mockedList.add("one");
        mockedList.clear();

        //verify
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    //demo of stubing
    static void stubTest(){
        //create mocked object
        LinkedList<String> mockedList = mock(LinkedList.class);

        //create stubing
        when(mockedList.get(0)).thenReturn("one");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //use stubing
        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(999));
        System.out.println(mockedList.get(1));
    }

    //demo of argument matcher
    static void argumentMatcher(){
        //create mocked object
        List<String> mockedList = mock(List.class);

        //matcher
        when(mockedList.get(anyInt())).thenReturn("ok");

        System.out.println(mockedList.get(999));
        System.out.println(mockedList.get(212));
    }

    //verify exact times of invocations
    static void invocationTimes(){
        //create mocked object
        List<String> mockedList = mock(List.class);

        //invocation
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        verify(mockedList,times(1)).add("once");
        verify(mockedList,atMost(2)).add("twice");
        verify(mockedList,atLeast(3)).add("three times");
        verify(mockedList,atMost(5)).add("three times");
    }
}
