package flink.query1;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import utils.Config;
import data.Event;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Date;



public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .process(new ProcessAllWindowFunction<Out1, Out1, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<Out1, Out1, TimeWindow>.Context context, Iterable<Out1> elements, Collector<Out1> out) throws Exception {

                        System.out.println("in processALL "+new Date(System.currentTimeMillis()));

                        for (Out1 element : elements) {
                            System.out.println(new Date(context.window().getStart()) + " " + element);
                        }

                        Out1 res = elements.iterator().next();
                        //out.collect(res);

                        Socket s = new Socket("localhost",6667);
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("CIAO");
                        dout.flush();
                        dout.close();
                        s.close();

                    }
                })
                .print()
                ;

    }
}
