package data;

import utils.Config;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return "Event{" +
                "symbol='" + symbol + '\'' +
                ", batch=" + batch +
                ", secType='" + secType + '\'' +
                ", timestamp=" + timestamp +
                ", strTimestamp='" + strTimestamp + '\'' +
                ", lastTradePrice=" + lastTradePrice +
                ", ema38=" + ema38 +
                ", ema100=" + ema100 +
                ", lastBatchTimestamp=" + lastBatchTimestamp +
                '}';
    }

    public static List<Event> createSymbolLastTsList(final List<Event> list){

        List<Event> temp = list;
        List<Event> symbolLastTsList;

        //sort the elements by symbol and find the max ts for that symbol
        symbolLastTsList = temp.stream()
                .collect(Collectors.groupingBy(Event::getSymbol,
                        Collectors.maxBy(Comparator.comparing(Event::getTimestamp))))
                .values().stream()
                .map(Optional::get)
                .collect(Collectors.toList());

        for (int i=0;i<symbolLastTsList.size();i++){
            System.out.println("-"+symbolLastTsList.get(i).getSymbol()+" - "+symbolLastTsList.get(i).getTimestamp());
        }

        return symbolLastTsList;

    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Timestamp getLastBatchTimestamp() {
        return lastBatchTimestamp;
    }

    public void setLastBatchTimestamp(Timestamp lastBatchTimestamp) {
        this.lastBatchTimestamp = lastBatchTimestamp;
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
