package flink.query1;

import scala.Tuple2;

import java.util.List;
import java.util.Map;

public class OutputQ1 {

    private Map<String, Float> lastPricePerSymbol;
    private Map<String, List<Integer>> symbolInBatches;

    public OutputQ1(Map<String, Float> price, Map<String, List<Integer>> batches) {
        this.lastPricePerSymbol = price;
        this.symbolInBatches = batches;

    }

    public static Map<Tuple2<String,Integer>, Float> calculateEMA(String s, Float lastPrice, int currWindowCount, int j, Map<Tuple2<String,Integer>, Float> myEma38){

        float lastEma = 0;    //retrieve last ema through key (currWindowCount)
        float resEma;

        int i=2;

        //System.out.println("currWindowCount: "+currWindowCount);
        if (currWindowCount>0){
            if (myEma38.containsKey(new Tuple2<>(s, currWindowCount-1))){
                lastEma = myEma38.get(new Tuple2<>(s, currWindowCount-1));
            } else {
                //todo: e se un simbolo stava in due finestre prima????
                //lastEma = 0;
                while (i!=currWindowCount){
                    if (myEma38.containsKey(new Tuple2<>(s, currWindowCount-i))){
                        lastEma = myEma38.get(new Tuple2<>(s, currWindowCount-i));
                        break;
                    }
                    i++;
                }
            }
        }

        resEma = (lastPrice*((float)2/(1+j)))+lastEma*(1-((float)2/(1+j)));
        myEma38.put(new Tuple2<>(s, currWindowCount), resEma);

        return myEma38;

   }


    public Map<String, Float> getLastPricePerSymbol() {
        return lastPricePerSymbol;
    }

    public void setLastPricePerSymbol(Map<String, Float> lastPricePerSymbol) {
        this.lastPricePerSymbol = lastPricePerSymbol;
    }

    public Map<String, List<Integer>> getSymbolInBatches() {
        return symbolInBatches;
    }

    public void setSymbolInBatches(Map<String, List<Integer>> symbolInBatches) {
        this.symbolInBatches = symbolInBatches;
    }

    @Override
    public String toString() {
        return "OutputQ1{" +
                "lastPrice=" + lastPricePerSymbol +
                '}';
    }
}
