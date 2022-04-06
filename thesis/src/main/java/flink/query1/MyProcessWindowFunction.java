package flink.query1;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Tuple2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow> {

    private Map<String, Integer> count;  //counts number of current window per symbol
    private Map<Tuple2<String,Integer>,Float> myEma38;   //K: <symbol,countWindow> - V: ema
    private Map<Tuple2<String,Integer>,Float> myEma100;

    @Override
    public void process(String s, ProcessWindowFunction<OutputQ1, OutputQ1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<OutputQ1> out) throws Exception {

        Date windowStartDate = new Date();
        windowStartDate.setTime(context.window().getStart());
        OutputQ1 res = elements.iterator().next();
        Map<String, Tuple2<Float, Integer>>  lastPrice = res.getLastPrice();

        //System.out.println("windowstart: "+windowStartDate);
        if (count==null){
            count = new HashMap<>();
            count.put(s,0);
            //System.out.println("NULL mapCnt = "+count.keySet()+", "+count.get(s)+" - "+s);
        }
        else {
            if (!count.containsKey(s)){
                count.put(s,0);
                //System.out.println("1mapCnt = "+count.keySet()+", "+count.get(s)+" - "+s);
            } else {
                count.put(s, count.get(s)+1);
                //System.out.println("2mapCnt = "+count.keySet()+", "+count.get(s)+" - "+s);
            }

        }

        System.out.println("FINAL: k= "+s+" v= "+count.get(s)+"  "+windowStartDate);


        if (myEma38==null){
            myEma38 = new HashMap<>();
            myEma38.put(new Tuple2<>(s,count.get(s)),null);
        }
        if (myEma100==null){
            myEma100 = new HashMap<>();
            myEma100.put(new Tuple2<>(s,count.get(s)),null);
        }


        //calcolo ema38
        OutputQ1.calculateEMA(s,lastPrice.get(s)._1, count.get(s), 38, myEma38);
        //calcolo ema100
        OutputQ1.calculateEMA(s, lastPrice.get(s)._1, count.get(s), 100, myEma100);

        //System.out.println("--IN PROCESS: key = "+s+",  - window start = "+date+ ", count = "+ windowCount +", lastPrice = "+elements.iterator().next().getLastPrice()+",  currEma38 = "+ema38.get(windowCount)+",  currEma100 = "+ema100.get(windowCount)+",   batchSTART: "+ Consumer.startEndTsPerBatch.get(0).f0+",   batchEND: "+ Consumer.startEndTsPerBatch.get(0).f1);

/*
        for (Tuple2<String, Integer> name: myEma38.keySet()) {
            if (name._1.equals(s)){
                String key = name.toString();
                String value = myEma38.get(name).toString();
                System.out.println("EMA38 window "+s+" "+windowStartDate+" - K: "+key +"   V: " + value);
            }
        }

        for (Integer name: ema100.keySet()) {
            String key = name.toString();
            String value = ema100.get(name).toString();
            System.out.println("EMA100 window "+s+" "+date+" - K: "+key + " V: " + value);
        }

         */

        //Map<Tuple2<String, Integer>, Float> res38 = new HashMap<>();
        Map<Tuple2<String, Integer>, Float> res38 = res.getEma38Result();
        //res38 = myEma38;
        res.setEma38Result(myEma38);
        for (Tuple2<String, Integer> symbolWindow: myEma38.keySet()) {
            if (symbolWindow._1.equals(s)){
                String key = symbolWindow.toString();
                String value = myEma38.get(symbolWindow).toString();
                System.out.println("EMA38 window "+s+" "+windowStartDate+" - K: "+key +"   V: " + value);
                res38.put(symbolWindow, Float.valueOf(value));
                //res38.put(new Tuple2<>(s, SymbolWindow._2), myEma38.get(SymbolWindow));
            }
        }

        System.out.println("proc- res: "+res);

        //todo NON DEVO FARE SET MA AGGIUNGERLA A QUELLA GIA ESISTENTE!
        //Map<Tuple2<String, Integer>, Float> bho38 = res.getEma38Result();
        //bho38.put()

        //res.setEma38Result(res38);

        out.collect(res);


    }
}
