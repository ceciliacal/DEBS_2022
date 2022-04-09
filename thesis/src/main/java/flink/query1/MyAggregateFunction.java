package flink.query1;

import data.Event;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;


public class MyAggregateFunction implements AggregateFunction<Event, AccumulatorQ1, OutputQ1> {

    @Override
    public AccumulatorQ1 createAccumulator() {
        return new AccumulatorQ1();
    }


    @Override
    public AccumulatorQ1 add(Event value, AccumulatorQ1 accumulator) {
        //System.out.println("-- IN AGGREGATE: event = "+value.getSymbol()+" "+value.getSecType()+" "+value.getTimestamp());
        accumulator.add(value);
        return accumulator;

    }

    @Override
    public OutputQ1 getResult(AccumulatorQ1 accumulator) {
        //System.out.println("STO IN AGGREGATE GET RESULT!!!!!!");
        return new OutputQ1(accumulator.getLastPricePerSymbol(), accumulator.getSymbolInBatches());
    }

    @Override
    public AccumulatorQ1 merge(AccumulatorQ1 a, AccumulatorQ1 b) {
        return a;
    }

}
