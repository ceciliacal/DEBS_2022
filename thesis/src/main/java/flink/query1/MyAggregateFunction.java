package flink.query1;

import data.Event;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.io.Serializable;

public class MyAggregateFunction implements AggregateFunction<Event, AccumulatorQ1, Object> {

    @Override
    public AccumulatorQ1 createAccumulator() {
        return new AccumulatorQ1();
    }


    @Override
    public AccumulatorQ1 add(Event value, AccumulatorQ1 accumulator) {
        System.out.println("-- IN AGGREGATE: event = "+value.getSymbol()+" "+value.getSecType()+" "+value.getTimestamp());
        //todo: if getBatch==1 + TS<lastTs
        //se event ha symbol nel batch (o il corrente o il successivo) calcolo l'ema quindi sempre in pratica
        accumulator.add(value);
        return accumulator;

        //TODO: capire ogni quanto viene chiamata ADD di aggregate. se è una volta sola per finestra
        // va messo qui la key dell'hashmap a 1, se è una volta per evento bisogna mettere la key costante sempre uguale
        // la si cambia nella process window function mettendo il numero della finestra.

    }

    @Override
    public Object getResult(AccumulatorQ1 accumulator) {
        return null;
    }

    @Override
    public AccumulatorQ1 merge(AccumulatorQ1 a, AccumulatorQ1 b) {
        return null;
    }
}
