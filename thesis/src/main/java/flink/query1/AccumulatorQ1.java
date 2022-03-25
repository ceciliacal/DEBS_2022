package flink.query1;

import data.Event;
import org.apache.flink.api.common.accumulators.Accumulator;
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

    //count curr window, ema curr window
    //window count starts from 0
    private Map<Integer, Double> windowEma38;   //TODO: essendo hashmap, faccio sempre il calcolo e lo sovrascrivo con ultimo price!!! quando si chiude la finestra
    private Map<Integer, Double> windowEma100;

    public AccumulatorQ1(){
        this.windowEma38 = new HashMap<>();
        this.windowEma100 = new HashMap<>();
    }

    public void add(Event value) {
        Double res;


        if (!windowEma38.containsKey(0)){
            //nothing
        }

    }

    public Serializable getLocalValue() {
        return null;
    }


    public void resetLocal() {

    }

    public void merge(Accumulator<Object, Serializable> other) {

    }

    @Override
    public Accumulator<Object, Serializable> clone() {
        return null;
    }
}

