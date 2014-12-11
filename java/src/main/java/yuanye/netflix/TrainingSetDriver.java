package yuanye.netflix;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created by krick on 12/11/2014.
 */
public class TrainingSetDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        if (args.length < 1){
            System.err.println("params: InputPath1,InputPath2...");
            System.exit(1);
        }
        final String table = "TrainingSet";

        Configuration conf = new Configuration();
        Job job = new Job(conf,"Import training set data from file to table " + table);

        job.setJarByClass(TrainingSetDriver.class);
        job.setMapperClass(TrainingSetMapper.class);
        job.setInputFormatClass(TrainingSetInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);
        job.setOutputKeyClass(ImmutableBytesWritable.class);
        job.setOutputValueClass(Put.class);
        job.setNumReduceTasks(0);

        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE,table);
        for (String arg : args){
            FileInputFormat.addInputPath(job, new Path(arg));
        }
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
