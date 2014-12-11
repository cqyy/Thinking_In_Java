package yuanye.complexModel.store;

public class Test {

    static public void main(String[] args) {
        int CHAR_TEST = 1000;
        for (int i = 0; i < CHAR_TEST; i++) {
            char ch = (char) i;
            for (int j = 0; j < CHAR_TEST; j++) {
                if (i != j) {
                    char ch2 = (char) j;
                    if (ch2 != ch) {
                        boolean b1 = Character.toUpperCase(ch) == Character.toUpperCase(ch2);
                        boolean b2 = Character.toLowerCase(ch) == Character.toLowerCase(ch2);
                        if (b1 && !b2) {
                            System.out.print("UPPER SAME FOR: " + i + " and " + j + ". ");
                            System.out.println("Ch1=" + ch + ". Ch2=" + ch2 + ".");
                        } else if (b2 && !b1) {
                            System.out.print("LOWER SAME FOR: " + i + " and " + j + ". ");
                            System.out.println("Ch1=" + ch + ". Ch2=" + ch2 + ".");
                        }
                    }
                }
            }
        }
    }
}

