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
                .setParallelism(3)
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .process(new ProcessAllWindowFunction<Out1, Out1, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<Out1, Out1, TimeWindow>.Context context, Iterable<Out1> elements, Collector<Out1> out) throws Exception {

                        int i = 0;
                        Out1 res = elements.iterator().next();
                        System.out.println("in processALL "+new Date(System.currentTimeMillis()));
                        String prova = "0,"+
                                res.getBatchNum()+","+
                                res.getSymbol()+","+
                                res.getSymbol_WindowEma38().get(res.getSymbol())._2+","+
                                res.getSymbol_WindowEma100().get(res.getSymbol())._2+","+
                                "BUY:"+res.getSymbol_buyCrossovers().get(res.getSymbol())+","+
                                "SELL:"+res.getSymbol_sellCrossovers().get(res.getSymbol())+"\n"
                                ;

                        for (Out1 element : elements) {
                            if (i==0){
                                prova = prova;
                            } else{
                                prova = prova +
                                        i+","+
                                        element.getBatchNum()+","+
                                        element.getSymbol()+","+
                                        element.getSymbol_WindowEma38().get(element.getSymbol())._2+","+
                                        element.getSymbol_WindowEma100().get(element.getSymbol())._2+","+
                                        "BUY:"+element.getSymbol_buyCrossovers().get(element.getSymbol())+","+
                                        "SELL:"+element.getSymbol_sellCrossovers().get(element.getSymbol())+"\n"
                                ;
                            }
                            System.out.println(new Date(context.window().getStart()) + " " + element);
                            i++;
                        }

                        Socket s = new Socket("localhost",6667);
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF(prova);
                        dout.flush();
                        dout.close();
                        s.close();
                        out.collect(res);

                    }
                })
                .print()
                ;

    }
}
