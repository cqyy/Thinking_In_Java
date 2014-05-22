package yuanye.algorithm;

import java.util.*;

/**
 * Created by Administrator on 14-3-31.
 * 找出字符串中第一个没有重复的字符，例如hello，除‘l‘之外都没有重复，但是第一个为’h‘
 */
public class FirstNoRepeatingChar {

    /**
     * 方法一：使用有序Map - LinkedhashMap保存String中每个字符的频率，
     * 然后在遍历Map，找到第一个频率为1的字符。
     * 想要两次循环
     */
    public static char firstNoRepeatingCharWay1(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        char[] chars = str.toCharArray();
        Map<Character, Integer> wordCount = new LinkedHashMap<>(chars.length);
        for (char c : chars) {
            wordCount.put(c, wordCount.containsKey(c)
                    ? wordCount.get(c) + 1
                    : 1);
        }

        for (Map.Entry<Character, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Not Found");
    }

    /**
     * 方式2，同时使用两个容器，同时保存没有重复的和已经重复的字符，这样，只
     * 需要一次循环就可以找到。但是比较浪费空间.
     *
     * @param str
     * @return
     */
    public static char firstNoRepeatingCharWay2(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        char[] chars = str.toCharArray();
        Set<Character> repeatingChar = new HashSet<>(chars.length);
        List<Character> noRepeatingChar = new ArrayList<>();

        for (char c : chars) {
            if (repeatingChar.contains(c)) {
                continue;
            }
            if (noRepeatingChar.contains(c)) {
                noRepeatingChar.remove((Character)c);
                repeatingChar.add(c);
            } else {
                noRepeatingChar.add(c);
            }
        }
        if (noRepeatingChar.size() == 0) {
            throw new RuntimeException("Not Found");
        }
        return noRepeatingChar.get(0);
    }

    /**
     * 方式3：使用无序Map存储词频，第二次扫描原字符串，找到第一个词频为1的字符
     *
     * @param str
     * @return
     */
    public static char firstNoRepeatingCharWay3(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        char[] chars = str.toCharArray();
        Map<Character, Integer> wordCount = new HashMap<>(chars.length);
        for (char c : chars) {
            wordCount.put(c, wordCount.containsKey(c) ? wordCount.get(c) + 1 : 1);
        }

        for (char c : chars) {
            if (wordCount.get(c) == 1) {
                return c;
            }
        }
        throw new RuntimeException("Not Found");

    }
}
