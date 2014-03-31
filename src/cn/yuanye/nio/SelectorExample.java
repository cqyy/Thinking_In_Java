package cn.yuanye.nio;

import java.io.FileNotFoundException;
import java.util.EnumSet;

/**
 * Created by Administrator on 14-3-16.
 */
public class SelectorExample {

    static enum fruit{Apple,Banana,Pear}

    public static void main(String[] args) throws FileNotFoundException {
        EnumSet<fruit> fruitEnumSet = EnumSet.of(fruit.Apple,fruit.Pear);
    }
}
