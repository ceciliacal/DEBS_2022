package flink.query1;

import kafka.Consumer;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow> {

    private Integer windowCount = 0;
    private float lastPrice = 0;
    public Map<Integer, Float> ema38;
    public Map<Integer, Float> ema100;

    @Override
    public void process(String s, ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<OutputQ1> out) throws Exception {
        Long windowStart = context.window().getStart();
        //context.window().getEnd();
        Date date = new Date();
        date.setTime(windowStart);
        OutputQ1 res = elements.iterator().next();
        lastPrice = res.getLastPrice();


        if (windowCount ==0){
            ema38 = new HashMap<>();
            ema100 = new HashMap<>();
        }

        //calcolo ema38
        OutputQ1.calculateEMA(lastPrice, windowCount, 38, ema38);
        //calcolo ema100
        OutputQ1.calculateEMA(lastPrice, windowCount, 100, ema100);

        System.out.println("--IN PROCESS: key = "+s+",  - window start = "+date+ ", windowCount = "+ windowCount +", lastPrice = "+elements.iterator().next().getLastPrice()+",  currEma38 = "+ema38.get(windowCount)+",  currEma100 = "+ema100.get(windowCount)+",   batchSTART: "+ Consumer.startEndTsPerBatch.get(0).f0+",   batchEND: "+ Consumer.startEndTsPerBatch.get(0).f1);

        for (Integer name: ema38.keySet()) {
            String key = name.toString();
            String value = ema38.get(name).toString();
            System.out.println("EMA38 window "+s+" "+date+" - K: "+key +"   V: " + value);
        }
        for (Integer name: ema100.keySet()) {
            String key = name.toString();
            String value = ema100.get(name).toString();
            System.out.println("EMA100 window "+s+" "+date+" - K: "+key + " V: " + value);
        }
        windowCount++;

        //per recuperare lastTs per symbol nel batch, magari posso fare una funzione che se gli passo la chiave e l'orario di chiusura di window controlla

        out.collect(res);

        //TODO: if orario window == lastTimestamp nel batch, salva i dati in una classe apposita cosi poi li stampi


    }
}
