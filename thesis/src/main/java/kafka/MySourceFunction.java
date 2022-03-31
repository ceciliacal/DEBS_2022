package kafka;

import data.Event;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.flink.configuration.Configuration;

//public class MySourceFunction extends RichParallelSourceFunction<String> {
public class MySourceFunction implements SourceFunction<String>{

    private volatile boolean isRunning = true;
    private String filePath;

    public MySourceFunction(String path) {
        this.filePath = path;
    }

    /*
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        isRunning = true;
    }
     */

    @Override
    public void run(SourceFunction.SourceContext<String> ctx) throws Exception {

        File file = new File(filePath);
        BufferedReader br = null;
        FileReader fr = new FileReader(file);
        br = new BufferedReader(fr);
        while (isRunning) {

            String line = "";
            while (isRunning && (line = br.readLine()) != null) {
                ctx.collect(line);
            }
            //br.close();
        }


/*
        while (isRunning){
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = br.readLine();
                String[] row;
                while (line!=null){
                    System.out.println("line = "+line);
                    ctx.collect(line);
                    line = br.readLine();
                }
            }
        }

 */

    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
