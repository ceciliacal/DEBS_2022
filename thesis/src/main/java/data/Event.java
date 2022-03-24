package data;

import utils.Config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Event {

    String      symbol;
    Integer     batch;
    String      secType;
    Timestamp   timestamp;
    String      strTimestamp;
    float       lastTradePrice;
    Double      ema38;
    Double      ema100;
    Timestamp   lastBatchTimestamp;


    public Event(String symbol, Integer batch, String secType, String strTimestamp, float lastTradePrice) {
        this.symbol = symbol;
        this.batch = batch;
        this.secType = secType;
        this.strTimestamp = strTimestamp;
        this.lastTradePrice = lastTradePrice;
        this.timestamp = stringToTimestamp(strTimestamp,batch);
    }

    public static boolean myContains(final List<Event> list, final String symbol, final String secType, final Timestamp ts, final float price){
        return list.stream().anyMatch(o -> o.getSymbol().equals(symbol) && o.getSecType().equals(secType)&& o.getTimestamp().equals(ts) && o.getLastTradePrice() == price);

    }



    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStrTimestamp() {
        return strTimestamp;
    }

    public void setStrTimestamp(String strTimestamp) {
        this.strTimestamp = strTimestamp;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public float getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(float lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
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

    /*
    Creates Timestamp object from symbol's last received update
     */
    public static Timestamp createTimestamp(String date, String time) {

        String dateTime = date+" "+time;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Config.pattern);
            Date parsedDate = dateFormat.parse(dateTime);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }

    public static Timestamp stringToTimestamp(String strDate, int invoker){

        SimpleDateFormat dateFormat = null;

        if (invoker==1){
            dateFormat = new SimpleDateFormat(Config.pattern);
        } else {
            dateFormat = new SimpleDateFormat(Config.pattern2);
        }

        try {
            Date parsedDate = dateFormat.parse(strDate);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            /*
            System.out.println("parsedDate.getTime() = "+parsedDate.getTime());
            System.out.println("parsedDate = "+parsedDate);
            System.out.println("strDate = "+strDate);
             */
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }


}
