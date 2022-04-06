package flink.query1;

import data.Event;

import kafka.Consumer;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        //TODO: TIENI TRACCIA DEI BATCH PER I RISULTATI!!!

        /*
        Processwindowfunction holds iteratable objects of all elements contained in a window,
        as well as additional meta information of the window to which the element belongs.
         */
        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(5)))
                .process(new ProcessAllWindowFunction<OutputQ1, String, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<OutputQ1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<String> out) throws Exception {

                        System.out.println("procALL-ouputQ1: "+elements.iterator().next());
                        OutputQ1 res = elements.iterator().next();
                        //System.out.println(elements.forEach(o -> o.getEma38Result()));


                        /*
                        Socket s = new Socket("localhost",6667);
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("CIAO");
                        dout.flush();
                        dout.close();
                        s.close();

                         */




                    }
                })
                .print()
        ;

    }
}
