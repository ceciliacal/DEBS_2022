package subscription;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.protobuf.Descriptors;
import data.Event;
import subscription.challenge.Batch;
import subscription.challenge.Benchmark;
import subscription.challenge.BenchmarkConfiguration;
import subscription.challenge.ChallengerGrpc;
import subscription.challenge.CrossoverEvent;
import subscription.challenge.Indicator;
import subscription.challenge.Query;
import subscription.challenge.ResultQ1;
import subscription.challenge.ResultQ2;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import kafka.Consumer;
import utils.Config;

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
            List<Indicator> q1Results = null;
            try {
                q1Results = calculateIndicators(batch);
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

    public static List<Indicator> calculateIndicators(Batch batch) throws Exception {

        Long seconds;
        int i;
        int num = batch.getEventsCount();
        List<Event> subSymbols = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);

        if (batch == null){
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

        FileWriter prova = new FileWriter("provaBatch.txt", true);

        for (i=0;i<num;i++){
            seconds = batch.getEvents(i).getLastTrade().getSeconds();
            Event event = new Event(batch.getEvents(i).getSymbol(),1,batch.getEvents(i).getSecurityType().toString().substring(0,1), formatter.format(new Date(seconds * 1000L)), batch.getEvents(i).getLastTradePrice());
            event.setPosition(i);
            subSymbols.add(event);

            System.out.println("subSymbols["+i+"] = " + subSymbols.get(i).getSymbol()+", "+subSymbols.get(i).getBatch()+", "+subSymbols.get(i).getSecType()+", "+subSymbols.get(i).getStrTimestamp()+", "+subSymbols.get(i).getLastTradePrice()+", "+subSymbols.get(i).getPosition());
            //METTI CAMPO PER LA GET (INDEX DELL EVENT NELLA LISTA + LAST TS LO DEVI PRENDERE QUA!


            /*
            if (i==0 || i==999){
                prova.write("subSymbols["+i+"] = " + subSymbols.get(i).getSymbol()+", "+subSymbols.get(i).getBatch()+", "+subSymbols.get(i).getSecType()+", "+subSymbols.get(i).getStrTimestamp()+", "+subSymbols.get(i).getLastTradePrice()+"\n");
            }
             */
        }
        prova.close();


        Consumer.startConsumer(subSymbols);
        return new ArrayList<>();

        //TODO: LAST BATCH!!!
    }

    public static List<CrossoverEvent> calculateCrossoverEvents(Batch batch) {
        //TODO: improve this implementation

        return new ArrayList<>();
    }
}
