package subscription;

import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import data.Event;
import kafka.Consumer;
import kafka.TestClass;
import subscription.challenge.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import utils.Config;

public class Main {

    public static Timestamp start;

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
            List<Indicator> q1Results = null;
            try {
                q1Results = calculateIndicators(batch, cnt);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    //cnt: # of batch
    public static List<Indicator> calculateIndicators(Batch batch, int cnt) throws Exception {

        Long seconds;
        int i;
        int num = batch.getEventsCount();
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);
        Map<String, Timestamp> subscribedSymbols = new HashMap<>();


        if (batch == null){
            return new ArrayList<>();
        }

        for (i=0;i<num;i++){
            seconds = batch.getEvents(i).getLastTrade().getSeconds();
            subscribedSymbols.put(batch.getEvents(i).getSymbol(), Event.stringToTimestamp(formatter.format(new Date(seconds * 1000L)),1));
        }


        System.out.println("===================subscribedSymbols: ");
        subscribedSymbols.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        if (cnt==0){
            start = Event.createTimestamp("08-11-2021","00:00:00.000");
        }
        TestClass.start(subscribedSymbols, cnt, start);

        //Consumer.startConsumer(subscribedSymbols);
        start = Collections.min(subscribedSymbols.values());


        return new ArrayList<>();
    }

     /*
        for (i=0;i<num;i++){
            System.out.println("----------- i = "+i+" -----------");
            System.out.println("getSymbol = "+batch.getEvents(i).getSymbol());
            System.out.println("getLastTradePrice = "+batch.getEvents(i).getLastTradePrice());
            seconds = batch.getEvents(i).getLastTrade().getSeconds();
            System.out.println("seconds = "+seconds);
            String dateString = formatter.format(new Date(seconds * 1000L));
            System.out.println("dateString = "+dateString);
        }
         */

    public static List<CrossoverEvent> calculateCrossoverEvents(Batch batch) {
        //TODO: improve this implementation

        return new ArrayList<>();
    }
}
