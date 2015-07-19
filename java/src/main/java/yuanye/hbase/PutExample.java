package yuanye.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by krick on 12/2/2014.
 */
public class PutExample {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
       // conf.addResource(new Path("./hbase-site.xml"));

        HTable htable = new HTable(conf,"testtable");
        Put put = new Put(Bytes.toBytes("rows1"));
        put.add(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"),Bytes.toBytes("val1"));
        put.add(Bytes.toBytes("colfam1"),Bytes.toBytes("qual2"),Bytes.toBytes("val2"));

        htable.put(put);
    }
}
