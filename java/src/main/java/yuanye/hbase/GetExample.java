package yuanye.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by cloudera on 12/3/14.
 */
public class GetExample {

    public static void main(String[] args) {
        Configuration conf = HBaseConfiguration.create();
        HTable hTable = null;
        try {
            hTable = new HTable(conf,"taobao_digital");
            Get get = new Get(Bytes.toBytes("10000087897"));
            get.addFamily(Bytes.toBytes("basic_info"));
            Result result = hTable.get(get);
            System.out.println(result);
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("basic_info"),Bytes.toBytes("name"))));
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage());
        }finally {
            if (hTable != null){
                try {
                    hTable.close();
                } catch (IOException e) {
                    System.err.println("Exception occurred when close HTable." + e.getMessage());
                }
            }
        }
    }
}
