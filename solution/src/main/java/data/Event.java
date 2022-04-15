package data;

import utils.Config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Event {

    String      symbol;
    Integer     batch;
    String      secType;
    Timestamp   timestamp;
    String      strTimestamp;
    float       lastTradePrice;
    Integer     numEvent;


    public Event(String symbol, Integer batch, String secType, String strTimestamp, float lastTradePrice, int numEvent) {
        this.symbol = symbol;
        this.batch = batch;
        this.secType = secType;
        this.strTimestamp = strTimestamp;
        this.lastTradePrice = lastTradePrice;
        this.timestamp = stringToTimestamp(strTimestamp,0);
        this.numEvent = numEvent;
    }

    @Override
    public String toString() {
        return "Event{" +
                "symbol='" + symbol + '\'' +
                ", batch=" + batch +
                ", secType='" + secType + '\'' +
                ", timestamp=" + timestamp +
                ", strTimestamp='" + strTimestamp + '\'' +
                ", lastTradePrice=" + lastTradePrice +
                ", numEvent=" + numEvent +
                '}';
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getNumEvent() {
        return numEvent;
    }

    public void setNumEvent(Integer numEvent) {
        this.numEvent = numEvent;
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

        if (invoker==0){
            dateFormat = new SimpleDateFormat(Config.pattern2);
        } else {
            dateFormat = new SimpleDateFormat(Config.pattern);
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
