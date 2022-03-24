package flink.query1;

import data.Event;
import flink.query1.MyAggregateFunction;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.List;

public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(event -> event.getSymbol());

        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .aggregate(new MyAggregateFunction(), new ProcessWindowFunction<Object, Object, String, TimeWindow>() {
                    @Override
                    public void process(String s, ProcessWindowFunction<Object, Object, String, TimeWindow>.Context context, Iterable<Object> elements, Collector<Object> out) throws Exception {
                        Long windowStart = context.window().getStart();
                        System.out.println("-- IN WINDOWFUNCTION: window start = "+windowStart);
                        Date date = new Date();
                        date.setTime(windowStart);
                        System.out.println("--  IN WINDOWFUNCTION: key = "+s+" window start DATE = "+windowStart);
                    }
                })
                .print()
                ;






    }
}
