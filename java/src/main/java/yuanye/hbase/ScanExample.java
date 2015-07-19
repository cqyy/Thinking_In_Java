package yuanye.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by cloudera on 12/3/14.
 */
public class ScanExample {
    public static void main(String[] args) {

        Configuration conf = HBaseConfiguration.create();
        HTable hTable = null;
        ResultScanner scanner = null;
        try{
            hTable = new HTable(conf,"taobao_digital");
            Scan scan = new Scan();
            byte[] family = Bytes.toBytes("basic_info");
            byte[] qufier = Bytes.toBytes("name");
            scan.addFamily(family);
            scanner = hTable.getScanner(scan);
            int count = 100;
            for (Result res : scanner){
                System.out.println(Bytes.toString(res.getValue(family,qufier)));
                if (count -- <= 0){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (hTable!= null){
                try {
                    hTable.close();
                } catch (IOException e) {
                    System.err.println("Exception occurred when close HTable." + e.getMessage());
                }
            }

            if (scanner != null){
               scanner.close();
            }
        }
    }
}
