package flink.query1;

import org.apache.flink.api.common.accumulators.Accumulator;
import java.io.Serializable;

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

    public AccumulatorQ1 AccumulatorQ1(){
        return this;
    }

    public void add(Object value) {

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

