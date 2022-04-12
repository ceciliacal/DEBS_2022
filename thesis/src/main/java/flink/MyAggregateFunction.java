package flink;

import data.Event;
import org.apache.flink.api.common.functions.AggregateFunction;


public class MyAggregateFunction implements AggregateFunction<Event, MyAccumulator, OutputQuery> {

    @Override
    public MyAccumulator createAccumulator() {
        return new MyAccumulator();
    }


    @Override
    public MyAccumulator add(Event value, MyAccumulator accumulator) {
        //System.out.println("-- IN AGGREGATE: event = "+value.getSymbol()+" "+value.getSecType()+" "+value.getTimestamp());
        accumulator.add(value);
        return accumulator;

    }

    @Override
    public OutputQuery getResult(MyAccumulator accumulator) {
        //System.out.println("STO IN AGGREGATE GET RESULT!!!!!!");
        return new OutputQuery(accumulator.getLastPricePerSymbol(), accumulator.getSymbolInBatches());
    }

    @Override
    public MyAccumulator merge(MyAccumulator a, MyAccumulator b) {
        return a;
    }

}
