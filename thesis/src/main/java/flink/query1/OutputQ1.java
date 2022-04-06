package flink.query1;

import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class OutputQ1 {

    private Map<String, Tuple2<Float, Integer>> lastPrice;   //todo batch in key
    private Map<Tuple2<String,Integer>, Float> ema38Result;
    private Map<Tuple2<String,Integer>, Float> ema100Result;

    public OutputQ1(Map<String, Tuple2<Float, Integer>> price) {
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

    public Map<Tuple2<String, Integer>, Float> getEma38Result() {
        return ema38Result;
    }

    public void setEma38Result(Map<Tuple2<String, Integer>, Float> ema38Result) {
        this.ema38Result = ema38Result;
    }

    public Map<String, Tuple2<Float, Integer>> getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Map<String, Tuple2<Float, Integer>> lastPrice) {
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
