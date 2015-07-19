package yuanye.dp;

/**
 * Created by Administrator on 14-4-9.
 */
public class Bridge {

    static abstract class TextFormat{

        public String formate(String str){
            return getFomater().format(str);
        };

        private TextFomaterImp fomater;

        private TextFomaterImp getFomater(){
            if (fomater == null){
                fomater = TextFormaterFactory.getFormater();
            }
            return fomater;
        };
    }

    static class TextFormat1 extends TextFormat{
        @Override
        public String formate(String str) {
            return " format1 " + super.formate(str);
        }
    }

    static abstract class TextFomaterImp{
        abstract String format(String str);
    }

    static class WindowsFomaterImp extends TextFomaterImp{
        String symbol = "\n\r";

        @Override
        String format(String str) {
            return str.replace("\\n",symbol);
        }
    }

    static class LinuxFormaterImp extends TextFomaterImp{
        String symbol = "\n";
        @Override
        String format(String str) {
            return str.replace("\\n",symbol);
        }
    }

    static class TextFormaterFactory{
       static TextFomaterImp getFormater(){
            return new WindowsFomaterImp();
        }
    }


    public static void main(String[] args){
        String text = "Hello , nice to hear your voice. \\n This should be a new line.";
        TextFormat format = new TextFormat1();
        System.out.println(format.formate(text));
    }
}
