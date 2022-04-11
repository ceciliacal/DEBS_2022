package flink.query1;

import scala.Tuple2;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Out1 {

    private String symbol;
    private String batches2;
    //private List<Integer> batches;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma38;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma100;
    private Float price;
    private Map<String, List<Timestamp>> symbol_buyCrossovers;
    private Map<String, List<Timestamp>> symbol_sellCrossovers;


    public Out1(String symbol, String batches2, Map<String, Tuple2<Integer, Float>> symbol_WindowEma38, Map<String, Tuple2<Integer, Float>> symbol_WindowEma100, Float price, Map<String, List<Timestamp>> symbol_buyCrossovers, Map<String, List<Timestamp>> symbol_sellCrossovers) {
        this.symbol = symbol;
        this.batches2 = batches2;
        this.symbol_WindowEma38 = symbol_WindowEma38;
        this.symbol_WindowEma100 = symbol_WindowEma100;
        this.price = price;
        this.symbol_buyCrossovers = symbol_buyCrossovers;
        this.symbol_sellCrossovers = symbol_sellCrossovers;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public Map<String, List<Timestamp>> getSymbol_buyCrossovers() {
        return symbol_buyCrossovers;
    }

    public void setSymbol_buyCrossovers(Map<String, List<Timestamp>> symbol_buyCrossovers) {
        this.symbol_buyCrossovers = symbol_buyCrossovers;
    }

    public Map<String, List<Timestamp>> getSymbol_sellCrossovers() {
        return symbol_sellCrossovers;
    }

    public void setSymbol_sellCrossovers(Map<String, List<Timestamp>> symbol_sellCrossovers) {
        this.symbol_sellCrossovers = symbol_sellCrossovers;
    }

    public String getBatches2() {
        return batches2;
    }

    public void setBatches2(String batches2) {
        this.batches2 = batches2;
    }

    @Override
    public String toString() {
        return "Out1{" +
                "symbol='" + symbol + '\'' +
                ", batches2='" + batches2 + '\'' +
                ", symbol_WindowEma38=" + symbol_WindowEma38 +
                ", symbol_WindowEma100=" + symbol_WindowEma100 +
                ", price=" + price +
                ", symbol_buyCrossovers=" + symbol_buyCrossovers +
                ", symbol_sellCrossovers=" + symbol_sellCrossovers +
                '}';
    }
}
