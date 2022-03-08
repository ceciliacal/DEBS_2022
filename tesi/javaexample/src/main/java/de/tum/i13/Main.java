package de.tum.i13;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.protobuf.Descriptors;
import de.tum.i13.challenge.Batch;
import de.tum.i13.challenge.Benchmark;
import de.tum.i13.challenge.BenchmarkConfiguration;
import de.tum.i13.challenge.ChallengerGrpc;
import de.tum.i13.challenge.CrossoverEvent;
import de.tum.i13.challenge.Indicator;
import de.tum.i13.challenge.Query;
import de.tum.i13.challenge.ResultQ1;
import de.tum.i13.challenge.ResultQ2;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Main {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("challenge.msrg.in.tum.de", 5023)
                //.forAddress("192.168.1.4", 5023) //in case it is used internally
                .usePlaintext()
                .build();


        var challengeClient = ChallengerGrpc.newBlockingStub(channel) //for demo, we show the blocking stub
                .withMaxInboundMessageSize(100 * 1024 * 1024)
                .withMaxOutboundMessageSize(100 * 1024 * 1024);

        BenchmarkConfiguration bc = BenchmarkConfiguration.newBuilder()
                .setBenchmarkName("Testrun " + new Date().toString())
                .addQueries(Query.Q1)
                .addQueries(Query.Q2)
                .setToken("jkninvezfgvcwexklizimkoonqmudupq") //go to: https://challenge.msrg.in.tum.de/profile/
                //.setBenchmarkType("evaluation") //Benchmark Type for evaluation
                .setBenchmarkType("test") //Benchmark Type for testing
                .build();

        //Create a new Benchmark
        Benchmark newBenchmark = challengeClient.createNewBenchmark(bc);

        //Start the benchmark
        challengeClient.startBenchmark(newBenchmark);

        //Process the events
        int cnt = 0;
        while(true) {
            Batch batch = challengeClient.nextBatch(newBenchmark);
            if (batch.getLast()) { //Stop when we get the last batch
                System.out.println("Received lastbatch, finished!");
                break;
            }

            //process the batch of events we have
            var q1Results = calculateIndicators(batch);

            ResultQ1 q1Result = ResultQ1.newBuilder()
                    .setBenchmarkId(newBenchmark.getId()) //set the benchmark id
                    .setBatchSeqId(batch.getSeqId()) //set the sequence number
                    .addAllIndicators(q1Results)
                    .build();

            //return the result of Q1
            challengeClient.resultQ1(q1Result);


            var crossOverevents = calculateCrossoverEvents(batch);

            ResultQ2 q2Result = ResultQ2.newBuilder()
                    .setBenchmarkId(newBenchmark.getId()) //set the benchmark id
                    .setBatchSeqId(batch.getSeqId()) //set the sequence number
                    .addAllCrossoverEvents(crossOverevents)
                    .build();

            challengeClient.resultQ2(q2Result);
            System.out.println("Processed batch #" + cnt);
            ++cnt;

            //todo: prima era 100
            if(cnt > 0) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
        }

        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");
    }

    private static List<Indicator> calculateIndicators(Batch batch) {
        //TODO: improve implementation
        //System.out.println("batch seqId = "+batch.getSeqId());
        System.out.println("-------------------start--------------------------");
        System.out.println("batch.getEvents(0)= "+batch.getEvents(0));
        System.out.println("batch.getEventsCount= "+batch.getEventsCount());
        System.out.println("batch.getEvents(0).getSymbol= "+batch.getEvents(0).getSymbol());

        Descriptors.FieldDescriptor fieldDescriptor = batch.getDescriptorForType().findFieldByName("seq_id");
        Object value = batch.getField(fieldDescriptor);
        System.out.println("value = "+batch.getAllFields());


        System.out.println("batch last = "+batch.getLast());
        System.out.println("---------------------end------------------------");

        return new ArrayList<>();
    }

    private static List<CrossoverEvent> calculateCrossoverEvents(Batch batch) {
        //TODO: improve this implementation

        return new ArrayList<>();
    }
}
