package subscription;

import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import subscription.challenge.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import utils.Config;

public class Main {

    private static long start;
    private static final Integer windowLen = 5; //minutes

    public static void main(String[] args) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSS");

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
        int num;
        Date nextWindow;

        //Start the benchmark
        challengeClient.startBenchmark(newBenchmark);

        //Process the events
        int cnt = 0;
        while(true) {

            //todo: mesa roba sopra la devo mettere qui in un if cnt ==0

            //while (batch(num)! next window

            Batch batch = challengeClient.nextBatch(newBenchmark);
            num = batch.getEventsCount();
            if (batch.getLast()) { //Stop when we get the last batch
                System.out.println("Received lastbatch, finished!");
                break;
            }

            if (cnt==0){
                start = batch.getEvents(0).getLastTrade().getSeconds() * 1000L;
                nextWindow = new Date(start + TimeUnit.MINUTES.toMillis((long) windowLen *(cnt+1)));
            }

            //nextWindow = new Date(nextWindow + TimeUnit.MINUTES.toMillis((long) windowLen *(cnt+1)));






            //TODO: while date(num) < next window, manda dati e poi dopo chiamo list indicator, gli passo ultimo batch e aspetta con la socket
            //process the batch of events we have
            List<Indicator> q1Results = null;
            q1Results = calculateIndicators(batch, cnt);


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
            if(cnt > 100) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
        }


        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");

    }

    //cnt: # of batch
    public static List<Indicator> calculateIndicators(Batch batch, int cnt) throws Exception {

        FileWriter myWriter = new FileWriter("batch_"+cnt+".txt");

        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);
        int i;
        int num = batch.getEventsCount();
        long seconds;

        for (i=0; i<num; i++) {

            myWriter.write("----------- i = " + i + " -----------\n");
            myWriter.write("getSymbol = " + batch.getEvents(i).getSymbol()+"\n");
            myWriter.write("getLastTradePrice = " + batch.getEvents(i).getLastTradePrice()+"\n");
            seconds = batch.getEvents(i).getLastTrade().getSeconds();
            //myWriter.write("seconds = " + seconds+"\n");
            String dateString = formatter.format(new Date(seconds * 1000L));
            myWriter.write("dateString = " + dateString+"\n");
        }
        myWriter.close();



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


    public static Timestamp stringToTimestamp(String strDate, int invoker){

        SimpleDateFormat dateFormat = null;

        if (invoker==0){
            dateFormat = new SimpleDateFormat(Config.pattern2);
        } else {
            dateFormat = new SimpleDateFormat(Config.pattern);
        }

        try {
            Date parsedDate = dateFormat.parse(strDate);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            /*
            System.out.println("parsedDate.getTime() = "+parsedDate.getTime());
            System.out.println("parsedDate = "+parsedDate);
            System.out.println("strDate = "+strDate);
             */
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }

}
