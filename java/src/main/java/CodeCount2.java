import java.io.*;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: swrite
 * Date: 13-5-10
 * Time: 上午12:23
 * 进行简单的代码行数统计
 */

/**ddd*/
public class CodeCount2 {
    static long  normalLine=0;
    static long commentLine=0;
    static long whiteLine=0;

    public static void p(Object obj){
        System.out.println(obj);
    }
    public static void countcode(File f){
        BufferedReader  br=null;
        boolean bln=false;
        try{
            br=new BufferedReader(new FileReader(f));
            String  line="";
            try {
                while((line = br.readLine()) != null) {
                    line=line.trim();
                    if(line.matches("^[\\s&&[^\\n]]*$")){
                        whiteLine+=1;
                    }else if(line.startsWith("/*")&&!line.equals("*/")){
                        commentLine+=1;
                        System.out.println(line);
                        bln=true;
                    }else if (bln==true){
                        commentLine+=1;
                        System.out.println(line);
                        if(line.endsWith("*/")){
                            bln=false;
                        }
                    }else if(line.startsWith("/*")&&line.endsWith("*/")){
                        commentLine+=1;
                        System.out.println(line);
                    }else if(line.startsWith("//")){
                        commentLine+=1;
                        System.out.println(line);
                    }else {
                        normalLine+=1;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static void main(String args[]){
        HashMap<String,String> map = new HashMap<>();
        map.put("Key1","Value1");
        System.out.println(map.get("Key1"));
        System.out.println(Arrays.toString("ddd ddd    ddd       ddd".split("\\s+")));
    }



}