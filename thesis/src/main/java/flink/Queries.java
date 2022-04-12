package flink;

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


public class Queries {

    public static void runQuery1(DataStream<Event> stream){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                .setParallelism(3)
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .process(new ProcessAllWindowFunction<FinalOutput, FinalOutput, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<FinalOutput, FinalOutput, TimeWindow>.Context context, Iterable<FinalOutput> elements, Collector<FinalOutput> out) throws Exception {

                        System.out.println("in processALL "+new Date(System.currentTimeMillis()));
                        int i = 0;
                        FinalOutput res = elements.iterator().next();  //first element in iterator

                        String stringToSend = "0;"+
                                res.getBatch()+";"+
                                res.getSymbol()+";"+
                                res.getSymbol_WindowEma38().get(res.getSymbol())._2+";"+
                                res.getSymbol_WindowEma100().get(res.getSymbol())._2+";"+
                                res.getSymbol_buyCrossovers().get(res.getSymbol())+";"+
                                res.getSymbol_sellCrossovers().get(res.getSymbol())+"\n"
                                ;

                        for (FinalOutput element : elements) {
                            if (i==0){
                                stringToSend = stringToSend;
                            } else{

                                stringToSend = stringToSend +
                                        i+";"+
                                        element.getBatch()+";"+
                                        element.getSymbol()+";"+
                                        element.getSymbol_WindowEma38().get(element.getSymbol())._2+";"+
                                        element.getSymbol_WindowEma100().get(element.getSymbol())._2+";"+
                                        element.getSymbol_buyCrossovers().get(element.getSymbol())+";"+
                                        element.getSymbol_sellCrossovers().get(element.getSymbol())+"\n"
                                        ;

                            }

                            System.out.println(new Date(context.window().getStart()) + " " + element);
                            i++;
                        }

                        Socket s = new Socket("localhost",6667);
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeBytes(stringToSend);
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
