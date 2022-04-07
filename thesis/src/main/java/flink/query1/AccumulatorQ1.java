package flink.query1;

import data.Event;
import org.apache.flink.api.common.accumulators.Accumulator;
import scala.Tuple2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
* Accumulators collect distributed statistics or aggregates in a from user functions and operators.
* Each parallel instance creates and updates its own accumulator object, and the different parallel
* instances of the accumulator are later merged. merged by the system at the end of the job. The
* result can be obtained from the result of a job execution, or from the web runtime monitor.
*
* <p>The accumulators are inspired by the Hadoop/MapReduce counters.
*
* <p>The type added to the accumulator might differ from the type returned. This is the case e.g.
* for a set-accumulator: We add single objects, but the result is a set of objects.
*
* @param <V> Type of values that are added to the accumulator
* @param <R> Type of the accumulator result as it will be reported to the client
*/

public class AccumulatorQ1 implements Serializable {

    //todo: qui x traccia BATCH Metti che value è una tupla2 di lastprice+ batchNumber
    //private Map<Tuple2<String,Integer>, Float> lastPricePerSymbol;  //K:symbol - V:last price
    private Map<String, Float> lastPricePerSymbol;  //K:symbol - V:last price
    private Map<String, Integer> batchCurrSymbol;   //K:symbol - V:batch number
    //private Map<Tuple2<String,Integer>, Float> lastPricePerSymbol2;  //K:symbol - V:last price


    public AccumulatorQ1(){
        this.lastPricePerSymbol = new HashMap<>();
        this.batchCurrSymbol = new HashMap<>();

    }

    public void add(Event value) {

        if (lastPricePerSymbol==null){
            lastPricePerSymbol = new HashMap<>();
            batchCurrSymbol = new HashMap<>();
        }
        //TODO METTI COME CHIAVE SIMBOLO E BATCH!!!
        lastPricePerSymbol.put(value.getSymbol(), value.getLastTradePrice());
        batchCurrSymbol.put(value.getSymbol(), value.getBatch());
        //lastPricePerSymbol.put(value.getSymbol(),new Tuple2<>(value.getLastTradePrice(),value.getBatch()));
        //System.out.println("ADDacc = "+value.getSymbol());

    }


    public Map<String, Float> getLastPricePerSymbol() {
        return lastPricePerSymbol;
    }

    public void setLastPricePerSymbol(Map<String, Float> lastPricePerSymbol) {
        this.lastPricePerSymbol = lastPricePerSymbol;
    }

    public Map<String, Integer> getBatchCurrSymbol() {
        return batchCurrSymbol;
    }

    public void setBatchCurrSymbol(Map<String, Integer> batchCurrSymbol) {
        this.batchCurrSymbol = batchCurrSymbol;
    }
}

