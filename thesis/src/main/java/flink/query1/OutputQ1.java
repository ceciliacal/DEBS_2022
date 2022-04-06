package flink.query1;

import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class OutputQ1 {

    private Map<String, Float> lastPrice;
    private Map<String, Tuple2<Integer, Float>> ema38Result;
    private Map<String, Tuple2<Integer, Float>> ema100Result;

    public OutputQ1(Map<String, Float> price) {
        this.lastPrice = price;
        this.ema38Result = new HashMap<>();
        this.ema100Result = new HashMap<>();
    }

    public static Map<Tuple2<String,Integer>, Float> calculateEMA(String s, Float lastPrice, int currWindowCount, int j, Map<Tuple2<String,Integer>, Float> myEma38){

        float lastEma;    //retrieve last ema through key (currWindowCount)
        float resEma;


        if (currWindowCount==0){
            lastEma = 0;
        } else {
            lastEma = myEma38.get(new Tuple2<>(s, currWindowCount-1));
        }

        resEma = (lastPrice*((float)2/(1+j)))+lastEma*(1-((float)2/(1+j)));
        myEma38.put(new Tuple2<>(s, currWindowCount), resEma);

        return myEma38;

   }

    public Map<String, Tuple2<Integer, Float>> getEma38Result() {
        return ema38Result;
    }

    public void setEma38Result(Map<String, Tuple2<Integer, Float>> ema38Result) {
        this.ema38Result = ema38Result;
    }

    public Map<String, Tuple2<Integer, Float>> getEma100Result() {
        return ema100Result;
    }

    public void setEma100Result(Map<String, Tuple2<Integer, Float>> ema100Result) {
        this.ema100Result = ema100Result;
    }

    public Map<String, Float> getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Map<String, Float> lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Override
    public String toString() {
        return "OutputQ1{" +
                "lastPrice=" + lastPrice +
                ", ema38Result=" + ema38Result +
                //", ema100Result=" + ema100Result +
                '}';
    }
}
