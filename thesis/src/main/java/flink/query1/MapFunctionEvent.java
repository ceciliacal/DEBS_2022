package flink.query1;

import data.Event;
import kafka.Consumer;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;


public class MapFunctionEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {

        //System.out.println("value = "+value);
        String line[] = value.split(",");

        Event event = new Event(line[0], Integer.parseInt(line[4]), line[1],line[2], Float.parseFloat(line[3]), Integer.parseInt(line[5]));

        if (event.getBatch()==0&&event.getNumEvent()==0){
            Consumer.setStartTime(event.getTimestamp().getTime());
        }

        event.setEma38(0.0);
        event.setEma100(0.0);

        //System.out.println("symbol= "+event.getSymbol());
        //System.out.println("event = "+event);

        return event;

    }
}

/*
//System.out.println("value = "+value);
        //String line[] = value.split(",");
        String ts = createTimestamp(line[2],line[3]).toString();
        Event event = new Event(line[0], 0,line[1], ts, Float.parseFloat(line[21]));
        System.out.println("event = "+event.toString());

        if (TestClass.subscribedSymbols.containsKey(event.getSymbol())){
            event.setBatch(1);
            event.setLastBatchTimestamp(TestClass.subscribedSymbols.get(event.getSymbol()));
            System.out.println("event IN BATCH = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());

        } else {
            event.setBatch(0);
            System.out.println("event NON IN BATCH  = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getTimestamp()+", "+event.getLastTradePrice()+", "+event.getLastBatchTimestamp());
        }

        event.setEma38(0.0);
        event.setEma100(0.0);
 */
