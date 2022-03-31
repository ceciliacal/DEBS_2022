package flink.query1;

import data.Event;

import kafka.Consumer;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;


public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        /*
        Processwindowfunction holds iteratable objects of all elements contained in a window,
        as well as additional meta information of the window to which the element belongs.
         */
        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                .print()
                ;










    }
}
