package flink.query1;

import kafka.Consumer;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Tuple2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQ1, Out1, String, TimeWindow> {

    private Map<Tuple2<String,Integer>,Float> myEma38;   //K: <symbol,countWindow> - V: ema
    private Map<Tuple2<String,Integer>,Float> myEma100;

    @Override
    public void process(String s, ProcessWindowFunction<OutputQ1, Out1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<Out1> out) throws Exception {

        long windowStart = context.window().getStart();
        Date windowStartDate = new Date();
        windowStartDate.setTime(windowStart);
        OutputQ1 res = elements.iterator().next();
        Map<String, Float> lastPricePerSymbol = res.getLastPricePerSymbol();
        Map<String, List<Integer>> symbolInBatches = res.getSymbolInBatches();

        int windowCount=0;
        while(true){
            if (Consumer.getStartTime()+(windowCount* TimeUnit.MINUTES.toMillis(5))!=windowStart){
                windowCount++;
            } else {
                break;
            }
        }

        //System.out.println("FINAL: k= "+s+" v= "+count.get(s)+"  "+windowStartDate);

        if (myEma38==null){
            myEma38 = new HashMap<>();
            myEma38.put(new Tuple2<>(s,windowCount),null);
        }
        if (myEma100==null){
            myEma100 = new HashMap<>();
            myEma100.put(new Tuple2<>(s,windowCount),null);
        }


        //calcolo ema38
        OutputQ1.calculateEMA(s,lastPricePerSymbol.get(s), windowCount, 38, myEma38);
        //calcolo ema100
        OutputQ1.calculateEMA(s, lastPricePerSymbol.get(s), windowCount, 100, myEma100);

        //System.out.println("--IN PROCESS: key = "+s+",  - window start = "+date+ ", count = "+ windowCount +", lastPrice = "+elements.iterator().next().getLastPrice()+",  currEma38 = "+ema38.get(windowCount)+",  currEma100 = "+ema100.get(windowCount)+",   batchSTART: "+ Consumer.startEndTsPerBatch.get(0).f0+",   batchEND: "+ Consumer.startEndTsPerBatch.get(0).f1);


        System.out.println(s+"  symbolInBatches = "+symbolInBatches.get(s));
        Map<String, Tuple2<Integer,Float>> symbol_WindowEma38 = new HashMap<>();
        Map<String, Tuple2<Integer,Float>> symbol_WindowEma100 = new HashMap<>();

        symbol_WindowEma38.put(s, new Tuple2<>(windowCount,myEma38.get(new Tuple2<>(s,windowCount))));
        symbol_WindowEma100.put(s, new Tuple2<>(windowCount,myEma100.get(new Tuple2<>(s,windowCount))));


        /*
        for (Tuple2<String, Integer> symbolWindow: myEma38.keySet()) {


            if (symbolWindow._1.equals(s)){
                String key = symbolWindow.toString();
                String value = myEma38.get(symbolWindow).toString();
                //System.out.println("EMA38 window "+s+" "+windowStartDate+" - K: "+key +"   V: " + value);

                //aiuto.put(s, new Tuple2<>(symbolWindow._2,Float.valueOf(value)));

            }
        }

         */


        //System.out.println("myEma38 = "+myEma38);
        //System.out.println("aiuto = "+aiuto);
        List<Integer> currBatches = symbolInBatches.get(s);
        currBatches.stream().forEach(batch -> {
            Out1 bho = new Out1(batch, symbol_WindowEma38, symbol_WindowEma100, lastPricePerSymbol.get(s));
            System.out.println("bho = "+bho);
            out.collect(bho);
        });


    }


}
