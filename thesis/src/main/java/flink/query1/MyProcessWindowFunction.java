package flink.query1;

import data.Event;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow> {

    private Integer count = 0;
    private float lastPrice = 0;
    public Map<Integer, Float> ema38;

    @Override
    public void process(String s, ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<OutputQ1> out) throws Exception {
        Long windowStart = context.window().getStart();
        //context.window().getEnd();
        Date date = new Date();
        date.setTime(windowStart);
        lastPrice = +elements.iterator().next().getLastPrice();

        if (count==0){
            ema38 = new HashMap<>();
        }


        //calcolo ema38
        OutputQ1.calculateEMA(lastPrice, count, 38, ema38);
        //ema38.put(count,currEma38);
        System.out.println("--  IN WINDOWFUNCTION: key = "+s+",  - window start DATE = "+date+ ", count = "+count+", lastPrice = "+elements.iterator().next().getLastPrice()+",  currEma38 = "+ema38.get(count));

        for (Integer name: ema38.keySet()) {
            String key = name.toString();
            String value = ema38.get(name).toString();
            System.out.println("window "+s+" "+date+" - K: "+key + " V: " + value);
        }
        count++;

        //TODO: if orario window == lastTimestamp nel batch, salva i dati in una classe apposita cosi poi li stampi

    }
}
