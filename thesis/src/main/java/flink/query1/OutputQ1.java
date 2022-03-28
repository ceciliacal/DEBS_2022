package flink.query1;

import java.util.HashMap;
import java.util.Map;

public class OutputQ1 {

    private float lastPrice;
    private Map<Integer, Float> ema38;
    private String symbol;

    public OutputQ1(float price) {
        this.lastPrice = price;

    }

    public OutputQ1(String key, Integer countWindow, Float ema38, Float ema100, Float lastPrice){
        this.symbol = key;
        this.lastPrice = lastPrice;
        this.ema38 = new HashMap<>();
        this.ema38.put(countWindow,ema38);

    }

    public static Map<Integer, Float> calculateEMA(Float lastPrice, int currWindowCount, int j, Map<Integer, Float> ema){

        float lastEma;    //retrieve last ema through key (windowCount)
        float resEma;

        if (currWindowCount==0){
            lastEma = 0;
        } else {
            lastEma = ema.get(currWindowCount-1);
        }

        resEma = (lastPrice*((float)2/(1+j)))+lastEma*(1-((float)2/(1+j)));
        //System.out.printf("ResEma ="+ resEma);
        ema.put(currWindowCount,resEma);

        return ema;

   }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Map<Integer, Float> getEma38() {
        return ema38;
    }

    public void setEma38(Map<Integer, Float> ema38) {
        this.ema38 = ema38;
    }
}
