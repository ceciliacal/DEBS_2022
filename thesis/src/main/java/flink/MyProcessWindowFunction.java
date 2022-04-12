package flink;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Tuple2;
import utils.Config;

import java.sql.Timestamp;
import java.util.*;

public class MyProcessWindowFunction extends ProcessWindowFunction<OutputQuery, FinalOutput, String, TimeWindow> {

    private Map<String, Integer> count;  //counts number of current window per symbol
    private Map<Tuple2<String,Integer>,Float> myEma38;   //K: <symbol,countWindow> - V: ema
    private Map<Tuple2<String,Integer>,Float> myEma100;
    private Map<String, List<Timestamp>> buyCrossovers;
    private Map<String, List<Timestamp>> sellCrossovers;



    @Override
    public void process(String s, ProcessWindowFunction<OutputQuery, FinalOutput, String, TimeWindow>.Context context, Iterable<OutputQuery> elements, Collector<FinalOutput> out) throws Exception {

        long windowStart = context.window().getStart();
        Date windowStartDate = new Date();
        windowStartDate.setTime(windowStart);
        Timestamp windowEndTs = new Timestamp(context.window().getEnd());
        OutputQuery res = elements.iterator().next();

        Map<String, Float> lastPricePerSymbol = res.getLastPricePerSymbol();
        Map<String, List<Integer>> symbolInBatches = res.getSymbolInBatches();

        if (count==null){
            count = new HashMap<>();
            count.put(s,0);
        }
        else {
            if (!count.containsKey(s)){
                count.put(s,0);
            } else {
                count.put(s, count.get(s)+1);
            }
        }


        if (myEma38==null){
            myEma38 = new HashMap<>();
            myEma38.put(new Tuple2<>(s,count.get(s)),null);
        }
        if (myEma100==null){
            myEma100 = new HashMap<>();
            myEma100.put(new Tuple2<>(s,count.get(s)),null);
        }

        //calculating ema38
        OutputQuery.calculateEMA(s,lastPricePerSymbol.get(s), count.get(s), Config.ema38, myEma38);
        //calculating ema100
        OutputQuery.calculateEMA(s, lastPricePerSymbol.get(s), count.get(s), Config.ema100, myEma100);



        //========== QUERY2 ============
        if (buyCrossovers ==null){
            buyCrossovers = new HashMap<>();
        }
        if (sellCrossovers ==null){
            sellCrossovers = new HashMap<>();
        }


        if (count.get(s)>0){
            if (myEma38.containsKey(new Tuple2<>(s,count.get(s)-1)) && myEma100.containsKey(new Tuple2<>(s,count.get(s)-1))){

                if (myEma38.get(new Tuple2<>(s,count.get(s))) > myEma100.get(new Tuple2<>(s,count.get(s)))) {
                    if (myEma38.get(new Tuple2<>(s,count.get(s)-1)) <= myEma100.get(new Tuple2<>(s,count.get(s)-1))){
                        //buy
                        System.out.println("BUY!! "+s);
                        if (!buyCrossovers.containsKey(s)){
                            List<Timestamp> ts = new ArrayList<>();
                            ts.add(windowEndTs);
                            buyCrossovers.put(s, ts);
                        } else {
                            List<Timestamp> ts = buyCrossovers.get(s);
                            ts.add(windowEndTs);
                            buyCrossovers.put(s,ts);
                        }

                    }
                }

                if (myEma38.get(new Tuple2<>(s,count.get(s))) < myEma100.get(new Tuple2<>(s,count.get(s)))){
                    if (myEma38.get(new Tuple2<>(s,count.get(s)-1)) >= myEma100.get(new Tuple2<>(s,count.get(s)-1))) {
                        //sell
                        System.out.println("SELL!! "+s);
                        if (!sellCrossovers.containsKey(s)){
                            List<Timestamp> ts = new ArrayList<>();
                            ts.add(windowEndTs);
                            sellCrossovers.put(s, ts);
                        } else {
                            List<Timestamp> ts = sellCrossovers.get(s);
                            ts.add(windowEndTs);
                            sellCrossovers.put(s,ts);
                        }

                    }
                }
            }
        }



        //========== END QUERY2 ============

        Map<String, Tuple2<Integer,Float>> symbol_WindowEma38 = new HashMap<>();
        Map<String, Tuple2<Integer,Float>> symbol_WindowEma100 = new HashMap<>();

        symbol_WindowEma38.put(s, new Tuple2<>(count.get(s),myEma38.get(new Tuple2<>(s,count.get(s)))));
        symbol_WindowEma100.put(s, new Tuple2<>(count.get(s),myEma100.get(new Tuple2<>(s,count.get(s)))));

        List<Timestamp> lastThreeBuys = null;
        if (buyCrossovers.get(s)!=null){
            int sizeBuy = buyCrossovers.get(s).size();
            lastThreeBuys = new ArrayList<>();
            if (sizeBuy>=3){
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-1));
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-2));
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-3));
            } else if (sizeBuy==2){
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-1));
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-2));
            } else if (sizeBuy==1){
                lastThreeBuys.add(buyCrossovers.get(s).get(sizeBuy-1));
            }
            System.out.println(s+" "+windowStartDate+" - lastThreeBuys = "+ lastThreeBuys);
        }

        List<Timestamp> lastThreeSells = null;
        if (sellCrossovers.get(s)!=null){
            int sizeSell = sellCrossovers.get(s).size();
            lastThreeSells = new ArrayList<>();
            if (sizeSell>=3){
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-1));
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-2));
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-3));
            } else if (sizeSell==2){
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-1));
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-2));
            } else if (sizeSell==1){
                lastThreeSells.add(sellCrossovers.get(s).get(sizeSell-1));
            }
            System.out.println(s+" "+windowStartDate+" - lastThreeSells = "+ lastThreeSells );
        }

        Map<String, List<Timestamp>> symbol_buyCrossovers = new HashMap<>();
        Map<String, List<Timestamp>> symbol_sellCrossovers = new HashMap<>();
        symbol_buyCrossovers.put(s,lastThreeBuys);
        symbol_sellCrossovers.put(s, lastThreeSells);


        List<Integer> currBatches = symbolInBatches.get(s);
        currBatches.stream().forEach(batch -> {
            FinalOutput finalOutput = new FinalOutput(s, batch, symbol_WindowEma38, symbol_WindowEma100, lastPricePerSymbol.get(s), symbol_buyCrossovers, symbol_sellCrossovers);
            out.collect(finalOutput);
        });

    }


}
