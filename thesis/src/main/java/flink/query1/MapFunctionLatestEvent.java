package flink.query1;

import data.Event;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;

import java.sql.Timestamp;

public class MapFunctionLatestEvent extends RichMapFunction<Event, Event> {

    private Timestamp max;

    @Override
    public Event map(Event value) throws Exception {

        if (value.getBatch()==0){
            return null;
        }else{
            if (max==null){ //1 VOLTA dentro
                System.out.println("MAX È NULL!!!!!!!");
                max = value.getTimestamp();
            } else{
                if(max.after(value.getTimestamp())){
                    max=value.getTimestamp();
                }
            }
        }
        return value;
    }
}
