package yuanye;

import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;

/**
 * Created by Administrator on 2014/9/7.
 */
public class Test {
    public static void main(String[] args) throws IOException, YarnException {
        YarnClient yarnclient = YarnClient.createYarnClient();
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(YarnConfiguration.RM_HOSTNAME,"192.168.1.20");
        yarnclient.init(conf);
        yarnclient.start();

        System.out.println(yarnclient.getYarnClusterMetrics());
    }
}

