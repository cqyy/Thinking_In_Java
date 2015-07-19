package yuanye.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.HTable;

import java.io.IOException;

/**
 * Created by cloudera on 12/4/14.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HTable hTable = new HTable(conf,"new.table");
        HColumnDescriptor hd = new HColumnDescriptor("fam1");
        hTable.getTableDescriptor().addFamily(hd);
        System.out.println(hTable);
    }
}
