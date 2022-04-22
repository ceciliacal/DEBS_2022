package flink;

import scala.Tuple2;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class FinalOutput {

    private String symbol;
    private Integer batch;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma38;
    private Map<String, Tuple2<Integer,Float>> symbol_WindowEma100;
    private Float price;
    private Map<String, List<Timestamp>> symbol_buyCrossovers;
    private Map<String, List<Timestamp>> symbol_sellCrossovers;


    public FinalOutput(String symbol, Integer batch, Map<String, Tuple2<Integer, Float>> symbol_WindowEma38, Map<String, Tuple2<Integer, Float>> symbol_WindowEma100, Float price, Map<String, List<Timestamp>> symbol_buyCrossovers, Map<String, List<Timestamp>> symbol_sellCrossovers) {
        this.symbol = symbol;
        this.batch = batch;
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

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }



    @Override
    public String toString() {
        return "FinalOutput{" +
                "symbol='" + symbol + '\'' +
                ", batch=" + batch +
                ", symbol_WindowEma38=" + symbol_WindowEma38 +
                ", symbol_WindowEma100=" + symbol_WindowEma100 +
                ", price=" + price +
                ", symbol_buyCrossovers=" + symbol_buyCrossovers +
                ", symbol_sellCrossovers=" + symbol_sellCrossovers +
                '}';
    }


}
