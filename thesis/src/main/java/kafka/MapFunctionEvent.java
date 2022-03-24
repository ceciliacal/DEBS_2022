package kafka;

import data.Event;
import org.apache.flink.api.common.functions.MapFunction;


public class MapFunctionEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {
        String line[] = value.split(",");
        Event event = new Event(line[0], 0,line[1], line[2], Float.parseFloat(line[3]));

        Event event1 = Consumer.myBatch.get(0);

        if (Event.myContains(Consumer.myBatch, event.getSymbol(), event.getSecType(), event.getTimestamp(), event.getLastTradePrice())){
            //TODO ottimizzazione: non creare due eventi sia in lista sia in stream ma lo prendo dalla lista e faccio return
            System.out.println("===MAP FUNCTION NELL'IF!!!!!!!");
            event.setBatch(1);

            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());

        } else {
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());

        }

        return event;
    }
}
