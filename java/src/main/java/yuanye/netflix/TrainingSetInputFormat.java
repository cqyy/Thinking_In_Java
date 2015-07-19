package yuanye.netflix;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by krick on 12/11/2014.
 *
 * InputFormat for training dataset.
 */
public class TrainingSetInputFormat extends FileInputFormat<LongWritable,Text> {


    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext)
            throws IOException, InterruptedException {
         return new TrainingSetRecordReader();
    }

    private static class TrainingSetRecordReader extends RecordReader<LongWritable, Text> {

        private  long fileLen;
        private  FileSystem fs;
        private  BufferedReader lineReader;
        private  LongWritable key;

        private long pos = 0;
        private Text value = new Text();

        public synchronized void close(){
            if (lineReader != null){
                try {
                    lineReader.close();
                } catch (IOException e) {
                    //ignored
                }
            }
        }

        @Override
        public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            Path path = ((FileSplit)inputSplit).getPath();
            fs = path.getFileSystem(taskAttemptContext.getConfiguration());
            FileStatus status = fs.getFileStatus(path);
            if (status.isDirectory()){
                throw new IOException("Not a file " + path);
            }
            fileLen = status.getLen();
            lineReader = new BufferedReader(new InputStreamReader(fs.open(path)));
            String firstLine = lineReader.readLine();
            pos += firstLine.length();
            if (!firstLine.trim().endsWith(":")){
                throw new IOException("Not a valid trainnig set data.The file " +
                        "should start with 'film number' followd by ':' "  + path);
            }
            firstLine = firstLine.trim();
            firstLine = firstLine.substring(0,firstLine.length()-1);
            key = new LongWritable(Long.parseLong(firstLine));
        }

        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException {
            String line = lineReader.readLine();
            if (line == null){
                //end of the file
                return false;
            }
            value.set(line);
            pos += line.length();
            return true;
        }

        @Override
        public LongWritable getCurrentKey() throws IOException, InterruptedException {
            return key;
        }

        @Override
        public Text getCurrentValue() throws IOException, InterruptedException {
            return value;
        }

        @Override
        public float getProgress() throws IOException {
            return (float)pos/(fileLen == 0 ? 1:fileLen);
        }
    }

    /**
     * Based on the file structure of training dataset,split input files to splits with one file to one split.
     * @param context
     * @throws IOException
     */
    @Override
    public List<InputSplit> getSplits(JobContext context) throws IOException {
        List<FileStatus> statuses = listStatus(context);
        List<InputSplit> splits = new ArrayList<>(statuses.size());
        for (FileStatus status : statuses){
            if (status.isDirectory()){
                throw new IOException("Not a file " + status.getPath());
            }
            FileSystem fs = status.getPath().getFileSystem(context.getConfiguration());
            BlockLocation[] locations = fs.getFileBlockLocations(status,0,status.getLen());
            //Using the locations of first block of the file
            splits.add(new FileSplit(status.getPath(),0,status.getLen(),locations[0].getHosts()));
        }
        return splits;
    }

}
