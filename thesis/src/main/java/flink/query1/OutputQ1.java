package flink.query1;

import java.util.Map;

public class OutputQ1 {

    private float lastPrice;
    //Map<Integer, Float> ema38;

    public OutputQ1(float price) {
        this.lastPrice = price;

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

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Override
    public String toString() {
        return "OutputQ1{" +
                "lastPrice=" + lastPrice +
                '}';
    }
}
