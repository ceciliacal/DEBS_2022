package data;

import org.apache.flink.api.common.functions.MapFunction;
import static kafka.Producer.stringToTimestamp;

public class MapSrcStreamToEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {
        String line[] = value.split(",");
        Event event = new Event(line[0], 0,line[1], stringToTimestamp(line[2]), Double.parseDouble(line[3]), stringToTimestamp(line[4]));
        System.out.println("RICEVUTO DA CONSUMER2: = "+line[0]+", "+0+", "+line[1]+", "+line[2]+", "+line[3]+", "+line[4]);
        System.out.println("event = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastTradeTime());
        return event;
    }
}
