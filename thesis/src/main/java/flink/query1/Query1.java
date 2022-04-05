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


public class Query1 {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        //TODO: vedi maxTimestamp() Method to get the maximum timestamp allowed for a given window.

        /*
        Processwindowfunction holds iteratable objects of all elements contained in a window,
        as well as additional meta information of the window to which the element belongs.
         */
        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                /*
                .trigger(new Trigger<Event, TimeWindow>() {
                    @Override
                    public TriggerResult onElement(Event element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
                        Date date = new Date();
                        date.setTime(timestamp);
                        Date date2 = new Date();
                        date2.setTime(window.getStart());
                        System.out.println("onELement -> " + element.getSymbol() + " timestamp = " + date + " startWindow = " + date2);
                        if (Consumer.startEndTsPerBatch.get(element.getBatch()).f1!=null){
                            if (Consumer.startEndTsPerBatch.get(element.getBatch()).f1.compareTo(new Timestamp(window.getStart())) >= 0){//se sta dopo o uguale
                                System.out.println("COMPARE, endBatch= "+Consumer.startEndTsPerBatch.get(element.getBatch()).f1+", startWindow= "+date2);
                                return TriggerResult.FIRE;
                            }
                        }


                        return TriggerResult.CONTINUE;

                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {

                    }
                })

                 */
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                /*
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(15)))
                .process(new ProcessAllWindowFunction<OutputQ1, String, TimeWindow>() {

                    @Override
                    public void process(ProcessAllWindowFunction<OutputQ1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<String> out) throws Exception {

                        Long windowStart = context.window().getStart();
                        //context.window().getEnd();
                        Date date = new Date();
                        date.setTime(windowStart);

                        System.out.println("IN PROC ALL -- start window = "+date);
                        System.out.println("ALL- OutputQ1: "+elements.iterator().next().getLastPrice());

                        Socket s = new Socket("localhost",6667);

                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("caccaaa ciao");
                        dout.flush();
                        dout.close();
                        s.close();
                    }
                })

                 */
                .print()
                ;

    }
}
