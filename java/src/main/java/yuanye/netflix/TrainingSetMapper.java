package yuanye.netflix;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by krick on 12/11/2014.
 */
public class TrainingSetMapper extends Mapper<LongWritable,Text,ImmutableBytesWritable,Put> {


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        /**
         * Value format :
         * CustomerID,Rating,Date
         - CustomerIDs range from 1 to 2649429, with gaps. There are 480189 users.
         - Ratings are on a five star (integral) scale from 1 to 5.
         - Dates have the format YYYY-MM-DD.
         */
        String vstr = value.toString();
        String[] values = vstr.split(",");
        if (values.length != 3){
            throw new IOException("Invalid value " + vstr);
        }
        //using combining key,<film id>-<customer id>
        byte[] rowKey = Bytes.toBytes(key.get() + "-" + values[0]);
        Put put = new Put(rowKey);

        put.add(Bytes.toBytes("rating"),Bytes.toBytes("rating"),Bytes.toBytes(values[1]));  //rating:rating => rating
        put.add(Bytes.toBytes("rating"),Bytes.toBytes("date"),Bytes.toBytes(values[2]));    //rating:date   => date

        context.write(new ImmutableBytesWritable(rowKey),put);

    }
}
