package kafka;

import data.Event;
import org.apache.flink.api.common.functions.MapFunction;


public class MapFunctionEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {

        String line[] = value.split(",");
        Event event = new Event(line[0], 0,line[1], line[2], Float.parseFloat(line[3]));

        if (Consumer.subscribedSymbols.containsKey(event.getSymbol())){
            event.setBatch(1);
            event.setLastBatchTimestamp(Consumer.subscribedSymbols.get(event.getSymbol()));
            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());

        } else {
            event.setBatch(0);
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());
        }

        event.setEma38(0.0);
        event.setEma100(0.0);

        /*
        if (Event.myContains(Consumer.batchEvents, line[0], line[1], Event.stringToTimestamp(line[2],0), Float.parseFloat(line[3]))){
            System.out.println("Consumer.batchEvents SIZE BEFORE FILTER: "+Consumer.batchEvents.size());
            Optional<Event> retrievedEvent = Event.myGet(Consumer.batchEvents, line[0], line[1], Event.stringToTimestamp(line[2],0), Float.parseFloat(line[3]));
            System.out.println("Consumer.batchEvents SIZE AFTER FILTER: "+Consumer.batchEvents.size());
            event = retrievedEvent.get();
            event.setBatch(1);
            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());

            Timestamp lastTsInBatch;

        } else {

            event = new Event(line[0], 0,line[1], line[2], Float.parseFloat(line[3]));
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice());
        }

        //if eventBatch == 1, allora setta il lastTs
        //nel primo if: se prova2 contiene quel simbolo, ritorna il lastts
        //prova2.myGet(evento nel batch)


         */
        return event;
    }
}
