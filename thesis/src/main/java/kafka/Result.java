package kafka;

import scala.Tuple4;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Result {

    String symbol;
    Float ema38;
    Float ema100;
    List<Timestamp> buys;
    List<Timestamp> sells;

    public Result(String symbol, Float ema38, Float ema100, List<Timestamp> buys, List<Timestamp> sells) {
        this.symbol = symbol;
        this.ema38 = ema38;
        this.ema100 = ema100;
        this.buys = buys;
        this.sells = sells;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getEma38() {
        return ema38;
    }

    public void setEma38(Float ema38) {
        this.ema38 = ema38;
    }

    public Float getEma100() {
        return ema100;
    }

    public void setEma100(Float ema100) {
        this.ema100 = ema100;
    }

    public List<Timestamp> getBuys() {
        return buys;
    }

    public void setBuys(List<Timestamp> buys) {
        this.buys = buys;
    }

    public List<Timestamp> getSells() {
        return sells;
    }

    public void setSells(List<Timestamp> sells) {
        this.sells = sells;
    }

    @Override
    public String toString() {
        return "Result{" +
                "symbol='" + symbol + '\'' +
                ", ema38=" + ema38 +
                ", ema100=" + ema100 +
                ", buys=" + buys +
                ", sells=" + sells +
                '}';
    }
}
