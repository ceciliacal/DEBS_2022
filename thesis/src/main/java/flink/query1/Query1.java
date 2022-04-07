package flink.query1;

import data.Event;

import kafka.Consumer;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Tuple2;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        KeyedStream<Event, Tuple2<String, Integer>> stream2 =
                stream.keyBy(new KeySelector<Event, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> getKey(Event value) throws Exception {
                        return new Tuple2<>(value.getSymbol(), value.getBatch());
                    }
                });

        //TODO: TIENI TRACCIA DEI BATCH PER I RISULTATI!!!

        /*
        Processwindowfunction holds iteratable objects of all elements contained in a window,
        as well as additional meta information of the window to which the element belongs.
         */
        stream2
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))

                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(5)))
                .process(new ProcessAllWindowFunction<Out1, Out1, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<Out1, Out1, TimeWindow>.Context context, Iterable<Out1> elements, Collector<Out1> out) throws Exception {

                        System.out.println("procALL-ouputQ1: "+elements.iterator().next());
                        System.out.println("---iterator: "+elements.iterator());


                        Iterator<Out1> itr=elements.iterator();

                        while(itr.hasNext())
                        {
                            System.out.println(new Date(context.window().getStart())+" "+itr.next());
                        }

                        Out1 res = elements.iterator().next();
                        //out.collect(res);
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
