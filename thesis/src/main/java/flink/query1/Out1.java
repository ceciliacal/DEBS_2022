package flink.query1;

import scala.Tuple2;

import java.util.Map;

public class Out1 {

    private Integer batchNum;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma;
    private Float price;

    public Out1(Integer batch, Map<String, Tuple2<Integer,Float>> symbolWindow_ema, float price) {
        this.batchNum = batch;
        this.symbol_WindowEma = symbolWindow_ema;
        this.price = price;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batch) {
        this.batchNum = batch;
    }

    public Map<String, Tuple2<Integer,Float>> getSymbol_WindowEma() {
        return symbol_WindowEma;
    }

    public void setSymbol_WindowEma(Map<String, Tuple2<Integer,Float>> symbol_WindowEma) {
        this.symbol_WindowEma = symbol_WindowEma;
    }

    @Override
    public String toString() {
        return "Out1{" +
                "batchNum=" + batchNum +
                ", symbol_WindowEma=" + symbol_WindowEma +
                ", price=" + price +
                '}';
    }
}
