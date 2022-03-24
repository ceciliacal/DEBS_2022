package kafka;

import data.Event;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Optional;


public class MapFunctionEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {

        String line[] = value.split(",");
        Event event = null;

        if (Event.myContains(Consumer.myBatch, line[0], line[1], Event.stringToTimestamp(line[2],0), Float.parseFloat(line[3]))){
            Optional<Event> retrievedEvent = Event.myGet(Consumer.myBatch, line[0], line[1], Event.stringToTimestamp(line[2],0), Float.parseFloat(line[3]));
            event = retrievedEvent.get();
            event.setBatch(1);
            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());
        } else {

            event = new Event(line[0], 0,line[1], line[2], Float.parseFloat(line[3]));
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());
        }

        event.setEma38(0.0);
        event.setEma100(0.0);

        //TODO: keep track of LAST EMA

        return event;
    }
}
