package flink;

import data.Event;
import kafka.Consumer;
import org.apache.flink.api.common.functions.MapFunction;

import java.io.PrintWriter;
import java.sql.Timestamp;


public class MapFunctionEvent implements MapFunction<String, Event> {

    @Override
    public Event map(String value) throws Exception {

        //System.out.println("value = "+value);
        String line[] = value.split(",");

        Event event = new Event(line[0], Integer.parseInt(line[4]), line[1],line[2], Float.parseFloat(line[3]), Integer.parseInt(line[5]));

        if (event.getBatch()==0&&event.getNumEvent()==0){
            Consumer.setStartTime(event.getTimestamp().getTime());
            System.out.println("map-time = "+new Timestamp(System.currentTimeMillis()));
        }

        return event;

    }
}
