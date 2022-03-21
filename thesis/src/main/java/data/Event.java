package data;

import java.sql.Timestamp;

public class Event {

    String  symbol;
    Integer batch;
    String  secType;
    Double  lastTradePrice;
    Timestamp    lastTradeTime;
    Double  ema38;
    Double  ema100;
    Timestamp timestamp;

    public Event(String symbol, Integer batch, String secType, Timestamp timestamp, Double lastTradePrice, Timestamp lastTradeTime) {
        this.symbol = symbol;
        this.batch = batch;
        this.secType = secType;
        this.timestamp = timestamp;
        this.lastTradePrice = lastTradePrice;
        this.lastTradeTime = lastTradeTime;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public Timestamp getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(Timestamp lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public Double getEma38() {
        return ema38;
    }

    public void setEma38(Double ema38) {
        this.ema38 = ema38;
    }

    public Double getEma100() {
        return ema100;
    }

    public void setEma100(Double ema100) {
        this.ema100 = ema100;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }




}
