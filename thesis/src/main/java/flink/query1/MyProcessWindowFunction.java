package flink.query1;

import kafka.Consumer;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Tuple2;
import utils.Config;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQ1, Out1, String, TimeWindow> {

    private Map<String, Integer> count;  //counts number of current window per symbol
    private Map<Tuple2<String,Integer>,Float> myEma38;   //K: <symbol,countWindow> - V: ema
    private Map<Tuple2<String,Integer>,Float> myEma100;
    private Map<String, List<Timestamp>> buyCrossovers2;
    private Map<String, List<Timestamp>> sellCrossovers2;

    private Map<Tuple2<String, Integer>,Tuple2<String, Timestamp>> buyCrossovers;
    private Map<Tuple2<String, Integer>,Tuple2<String, Timestamp>> sellCrossovers;


    @Override
    public void process(String s, ProcessWindowFunction<OutputQ1, Out1, String, TimeWindow>.Context context, Iterable<OutputQ1> elements, Collector<Out1> out) throws Exception {

        long windowStart = context.window().getStart();
        Date windowStartDate = new Date();
        windowStartDate.setTime(windowStart);
        Timestamp windowEndTs = new Timestamp(context.window().getEnd());
        OutputQ1 res = elements.iterator().next();
        Map<String, Float> lastPricePerSymbol = res.getLastPricePerSymbol();
        Map<String, List<Integer>> symbolInBatches = res.getSymbolInBatches();

        int currentWindowCount=0;
        while(true){
            if (Consumer.getStartTime()+(currentWindowCount* TimeUnit.MINUTES.toMillis(5))!=windowStart){
                currentWindowCount++;
            } else {
                break;
            }
        }

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

        //System.out.println("window: "+windowStartDate+" - "+windowEndTs);
        //System.out.println("FINAL: k= "+s+" v= "+count.get(s)+"  "+windowStartDate);

        if (myEma38==null){
            myEma38 = new HashMap<>();
            myEma38.put(new Tuple2<>(s,count.get(s)),null);
        }
        if (myEma100==null){
            myEma100 = new HashMap<>();
            myEma100.put(new Tuple2<>(s,count.get(s)),null);
        }

        //calcolo ema38
        OutputQ1.calculateEMA(s,lastPricePerSymbol.get(s), count.get(s), Config.ema38, myEma38);
        //calcolo ema100
        OutputQ1.calculateEMA(s, lastPricePerSymbol.get(s), count.get(s), Config.ema100, myEma100);

        //System.out.println("--IN PROCESS: key = "+s+",  - window start = "+date+ ", count = "+ windowCount +", lastPrice = "+elements.iterator().next().getLastPrice()+",  currEma38 = "+ema38.get(windowCount)+",  currEma100 = "+ema100.get(windowCount)+",   batchSTART: "+ Consumer.startEndTsPerBatch.get(0).f0+",   batchEND: "+ Consumer.startEndTsPerBatch.get(0).f1);


        //========== QUERY2 ============
        if (buyCrossovers2==null){
            buyCrossovers2 = new HashMap<>();
        }
        if (sellCrossovers2==null){
            sellCrossovers2 = new HashMap<>();
        }



        float temp0 = myEma38.get(new Tuple2<>(s,0));
        float temp2 = 0;
        float temp3 = 0;
        float temp4 = 0;
        //======= PROVA QUERY2 CON IEBBB. TODO: DOPO TOGLI! ===========
        if (s.equals("IEBBB.FR") && count.get(s)==1){
            myEma38.put(new Tuple2<>(s,count.get(s)-1), (float) -1);    //BUY
        }

        if (s.equals("IEBBB.FR") && count.get(s)==2){
            temp2 = myEma38.get(new Tuple2<>(s,1));
            myEma38.put(new Tuple2<>(s,count.get(s)-1), (float) -1);
        }


        if (s.equals("IEBBB.FR") && count.get(s)==3){
            temp3 = myEma38.get(new Tuple2<>(s,1));
            myEma38.put(new Tuple2<>(s,count.get(s)-1), (float) -1);
        }
        if (s.equals("IEBBB.FR") && count.get(s)==4){
            temp4 = myEma38.get(new Tuple2<>(s,1));
            myEma38.put(new Tuple2<>(s,count.get(s)-1), (float) -1);
        }





        //======= FINE PROVA QUERY2 CON IEBBB. TODO: DOPO TOGLI! ===========




        if (count.get(s)>0){

            if (myEma38.containsKey(new Tuple2<>(s,count.get(s)-1)) && myEma100.containsKey(new Tuple2<>(s,count.get(s)-1))){

                if (myEma38.get(new Tuple2<>(s,count.get(s))) > myEma100.get(new Tuple2<>(s,count.get(s)))) {
                    if (myEma38.get(new Tuple2<>(s,count.get(s)-1)) <= myEma100.get(new Tuple2<>(s,count.get(s)-1))){
                        //buy
                        System.out.println("BUY!! "+s);
                        if (!buyCrossovers2.containsKey(s)){
                            List<Timestamp> ts = new ArrayList<>();
                            ts.add(windowEndTs);
                            buyCrossovers2.put(s, ts);
                        } else {
                            List<Timestamp> ts = buyCrossovers2.get(s);
                            ts.add(windowEndTs);
                            buyCrossovers2.put(s,ts);
                        }

                    }
                }

                if (myEma38.get(new Tuple2<>(s,count.get(s))) < myEma100.get(new Tuple2<>(s,count.get(s)))){
                    if (myEma38.get(new Tuple2<>(s,count.get(s)-1)) >= myEma100.get(new Tuple2<>(s,count.get(s)-1))) {
                        //sell
                        System.out.println("SELL!!");
                        if (!sellCrossovers2.containsKey(s)){
                            List<Timestamp> ts = new ArrayList<>();
                            ts.add(windowEndTs);
                            sellCrossovers2.put(s, ts);
                        } else {
                            List<Timestamp> ts = sellCrossovers2.get(s);
                            ts.add(windowEndTs);
                            sellCrossovers2.put(s,ts);
                        }

                    }
                }
            }
        }



        //========== END QUERY2 ============

        Map<String, Tuple2<Integer,Float>> symbol_WindowEma38 = new HashMap<>();
        Map<String, Tuple2<Integer,Float>> symbol_WindowEma100 = new HashMap<>();

        symbol_WindowEma38.put(s, new Tuple2<>(count.get(s),myEma38.get(new Tuple2<>(s,count.get(s)))));
        symbol_WindowEma100.put(s, new Tuple2<>(currentWindowCount,myEma100.get(new Tuple2<>(s,count.get(s)))));


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
        List<Timestamp> lastThreeBuys = null;
        if (buyCrossovers2.get(s)!=null){
            int sizeBuy = buyCrossovers2.get(s).size();
            lastThreeBuys = new ArrayList<>();
            if (sizeBuy>=3){
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-1));
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-2));
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-3));
            } else if (sizeBuy==2){
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-1));
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-2));
            } else if (sizeBuy==1){
                lastThreeBuys.add(buyCrossovers2.get(s).get(sizeBuy-1));
            }
            System.out.println("lastThreeBuys = "+ lastThreeBuys);
        }

        List<Timestamp> lastThreeSells = null;
        if (sellCrossovers2.get(s)!=null){
            int sizeSell = sellCrossovers2.get(s).size();
            lastThreeSells = new ArrayList<>();
            if (sizeSell>=3){
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-1));
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-2));
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-3));
            } else if (sizeSell==2){
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-1));
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-2));
            } else if (sizeSell==1){
                lastThreeSells.add(sellCrossovers2.get(s).get(sizeSell-1));
            }
            System.out.println("lastThreeSells = "+ lastThreeSells);
        }

        Map<String, List<Timestamp>> symbol_buyCrossovers = new HashMap<>();
        Map<String, List<Timestamp>> symbol_sellCrossovers = new HashMap<>();
        symbol_buyCrossovers.put(s,lastThreeBuys);
        symbol_sellCrossovers.put(s, lastThreeSells);

        System.out.println("ISBIW.FR: "+symbol_buyCrossovers.get("ISBIW.FR"));


        List<Integer> currBatches = symbolInBatches.get(s);
        currBatches.stream().forEach(batch -> {
            Out1 bho = new Out1(s, batch, symbol_WindowEma38, symbol_WindowEma100, lastPricePerSymbol.get(s), symbol_buyCrossovers, symbol_sellCrossovers);
            //System.out.println("bho = "+bho);
            out.collect(bho);
        });



        if (s.equals("IEBBB.FR")&&count.get(s)==1){
            myEma38.put(new Tuple2<>(s,1), temp0);

        }

        if (s.equals("IEBBB.FR")&&count.get(s)==2){
            myEma38.put(new Tuple2<>(s,2), temp2);
        }


        if (s.equals("IEBBB.FR")&&count.get(s)==3){
            myEma38.put(new Tuple2<>(s,3), temp3);
        }
        if (s.equals("IEBBB.FR")&&count.get(s)==4){
            myEma38.put(new Tuple2<>(s,4), temp4);
        }






    }


}
