package flink.query1;

import scala.Tuple2;

import java.util.Map;

public class Out1 {

    private Integer batchNum;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma38;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma100;
    private Float price;

    public Out1(Integer batch, Map<String, Tuple2<Integer,Float>> symbolWindow_ema38,Map<String, Tuple2<Integer,Float>>symbolWindow_ema100, float price) {
        this.batchNum = batch;
        this.symbol_WindowEma38 = symbolWindow_ema38;
        this.symbol_WindowEma100 = symbolWindow_ema100;
        this.price = price;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batch) {
        this.batchNum = batch;
    }

    public Map<String, Tuple2<Integer,Float>> getSymbol_WindowEma38() {
        return symbol_WindowEma38;
    }

    public void setSymbol_WindowEma38(Map<String, Tuple2<Integer,Float>> symbol_WindowEma38) {
        this.symbol_WindowEma38 = symbol_WindowEma38;
    }

    public Map<String, Tuple2<Integer, Float>> getSymbol_WindowEma100() {
        return symbol_WindowEma100;
    }

    public void setSymbol_WindowEma100(Map<String, Tuple2<Integer, Float>> symbol_WindowEma100) {
        this.symbol_WindowEma100 = symbol_WindowEma100;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Out1{" +
                "batchNum=" + batchNum +
                ", symbol_WindowEma38=" + symbol_WindowEma38 +
                ", price=" + price +
                '}';
    }
}
