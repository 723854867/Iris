package interview.collection;


import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djyin on 14-9-23.
 */
public class GoArrayList {
    // 测试下
    public static void main(String[] args) {
        List<String> metas = new ArrayList<String>(1000);
        for (int i = 0; i < 1000; i++) {
            metas.add("this is test " + i);
        }
        StopWatch sw = new LoggingStopWatch("w1");
        sw.start();
        for (int i = 0; i < 5000; i++) {

            String[] outputs = metas.toArray(new String[]{});
        }
        sw.stop();
        sw.start();
        sw = new LoggingStopWatch("w2");
        for (int i = 0; i < 5000; i++) {
            String[] outputs = metas.toArray(new String[metas.size()]);
        }

        sw.stop();
    }
}
