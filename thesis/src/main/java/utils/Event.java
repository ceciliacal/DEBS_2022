package utils;

public class Event {

    String  symbol;
    Integer batch;
    String  secType;
    Double  lastTradePrice;
    Long    lastTradeTime;
    Double  ema38;
    Double  ema100;

    public Event(String symbol, Integer batch, String secType, Double lastTradePrice, Long lastTradeTime) {
        this.symbol = symbol;
        this.batch = batch;
        this.secType = secType;
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

    public Long getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(Long lastTradeTime) {
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


}
