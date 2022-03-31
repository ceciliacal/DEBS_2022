package kafka;

import data.Event;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;

import static data.Event.createTimestamp;

//public class MySourceFunction extends RichParallelSourceFunction<String> {
public class MySourceFunction implements SourceFunction<Event>{

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
    public void run(SourceContext<Event> ctx) throws Exception {

        File file = new File(filePath);
        BufferedReader br = null;
        FileReader fr = new FileReader(file);
        br = new BufferedReader(fr);

        while (isRunning) {
            String line = "";
            Timestamp ts;
            String[] fields;

            while (isRunning && (line = br.readLine()) != null) {
                fields = line.split(",");
                ts = createTimestamp(fields[2],fields[3]);
                assert ts != null;

                //read data between end (lastTs) of previous batch and end of current batch
                if ((ts.compareTo(TestClass.startBatchTs) == 0) || (ts.compareTo(TestClass.startBatchTs) > 0)){
                    if ((ts.compareTo(TestClass.endBatchTs) == 0) || (ts.compareTo(TestClass.endBatchTs) < 0)){
                        Event event = createEvent(fields, ts);
                        ctx.collectWithTimestamp(event, event.getTimestamp().getTime());
                        //ctx.collect(event);

                    }
                }
            }
        }
    }

    public Event createEvent(String[] line, Timestamp ts) throws Exception{

        Event event = new Event(line[0], 0,line[1], ts.toString(), Float.parseFloat(line[21]));
        System.out.println("event = "+event.toString());

        if (TestClass.subscribedSymbols.containsKey(event.getSymbol())){
            event.setBatch(1);
            event.setLastBatchTimestamp(TestClass.subscribedSymbols.get(event.getSymbol()));
            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());

        } else {
            event.setBatch(0);
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());
        }

        long bho= event.getTimestamp().getTime();
        System.out.println("==long bho = "+ bho);
        System.out.println("==Timestamp(bho) = "+ new Timestamp(bho));

        event.setEma38(0.0);
        event.setEma100(0.0);

        return event;

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



    @Override
    public void cancel() {
        isRunning = false;
    }
}
