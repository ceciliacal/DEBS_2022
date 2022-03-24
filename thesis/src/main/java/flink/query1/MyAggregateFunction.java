package flink.query1;

import data.Event;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.io.Serializable;

public class MyAggregateFunction implements AggregateFunction<Event, Object, Object> {

    @Override
    public AccumulatorQ1 createAccumulator() {
        return new AccumulatorQ1();
    }


    @Override
    public Object add(Event value, Object accumulator) {
        //System.out.println("-- IN AGGREGATE: event = "+value.getSymbol()+" "+value.getSecType()+" "+value.getTimestamp()+" "+value.getLastTradeTime());
        return null;
    }

    @Override
    public Object getResult(Object accumulator) {
        return null;
    }

    @Override
    public Object merge(Object a, Object b) {
        return null;
    }
}
