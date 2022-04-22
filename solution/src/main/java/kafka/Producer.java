package kafka;

import de.tum.i13.challenge.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import scala.Tuple2;
import utils.Config;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Producer {

    protected static final Integer windowLen = 5; //minutes
    protected static Map<Integer,Long> batchSeqId;       //to retrieve each bach when sending aggregated results
    protected static Map<Tuple2<Integer, String>, Tuple2<Float, Float>> intermediateResults;      //K: #batch+symbol V:ema38+ema100
    protected static Timestamp finalWindowLongBatch = null ;
    protected static Map<Integer,List<Result>> finalResults;
    protected static Integer longBatch;
    protected static Timestamp currentTimestamp;
    protected static Integer cnt;
    protected static Timestamp prev;
    protected static boolean mustStop;


    //creates kafka producer
    public static org.apache.kafka.clients.producer.Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }


    //kafka producer streams messages to kafka topic reading csv file
    public static void main(String[] args) throws Exception {

        final org.apache.kafka.clients.producer.Producer<String, String> producer = createProducer();
        batchSeqId = new HashMap<>();
        intermediateResults = new HashMap<>();
        finalResults = new HashMap<>();
        longBatch = -1;
        mustStop = false;

        int port = Integer.parseInt(args[0]);
        //int port = 6668;
        //System.out.println("arg = "+args[0]);

        //============================ starts MAIN gRPC ============================

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
                .setToken("000000000000000000000000000000") //go to: https://challenge.msrg.in.tum.de/profile/
                .setBenchmarkType("evaluation") //Benchmark Type for evaluation
                //.setBenchmarkType("test") //Benchmark Type for testing
                .build();

        //Create a new Benchmark
        Benchmark newBenchmark = challengeClient.createNewBenchmark(bc);
        System.out.println("newBenchmark = "+newBenchmark);

        //Start the benchmark
        challengeClient.startBenchmark(newBenchmark);

        KafkaConsumerResults kafkaConsumer = new KafkaConsumerResults(newBenchmark, challengeClient);
        System.out.println("challengeClient = "+challengeClient);
        new Thread(()->{
            try {
                kafkaConsumer.runConsumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        //PrintWriter writer = new PrintWriter("/tmp/procTimes.txt", "UTF-8");
        PrintWriter writer = new PrintWriter("procTimes.txt", "UTF-8");

        //Process the events
        cnt = 0;
        int i;
        long currSeconds;
        int num;
        long start;
        Timestamp nextWindow = null;
        long next = 0;
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);
        int firstSendAfterReceive = 1;
        long startProcTime = 0;
        long resultsProcTime;
        double timeToProcessAWindow;

        while(true) {

            System.out.println("==== cnt: "+cnt);
            Batch batch = challengeClient.nextBatch(newBenchmark);
            if (batch==null){
                batch = challengeClient.nextBatch(newBenchmark);
            }

            batchSeqId.put(cnt,batch.getSeqId());
            num = batch.getEventsCount();

            if (batch.getLast()) { //Stop when we get the last batch
                System.out.println("Received lastbatch, finished!");
                break;
            }


            //======= windows setup ========
            if (cnt==0){
                start = batch.getEvents(0).getLastTrade().getSeconds() * 1000L;
                next = start + TimeUnit.MINUTES.toMillis(windowLen);
                nextWindow = new Timestamp(next);
                //System.out.println("nextWindow = "+nextWindow);
            }
            //==== end of windows setup =====


            String[][] value = {new String[7]};
            final String[] valueToSend = new String[1];

            long lastTsSeconds = batch.getEvents(num-1).getLastTrade().getSeconds();
            Timestamp lastTsBatch = stringToTimestamp(formatter.format(new Date(lastTsSeconds * 1000L)),1);
            long startTsSeconds = batch.getEvents(0).getLastTrade().getSeconds();
            Timestamp startTsBatch = stringToTimestamp(formatter.format(new Date(startTsSeconds * 1000L)),1);

            System.out.println("startTsBatch = "+startTsBatch);
            System.out.println("lastTsBatch = "+lastTsBatch);

            if (lastTsSeconds - startTsSeconds > TimeUnit.MINUTES.toSeconds(windowLen)){
                longBatch=cnt;
                setFinalWindowLongBatch(windowProducingResult(lastTsBatch,nextWindow));
                //System.out.println("batch "+longBatch+" size is bigger than just one window.");
                //System.out.println("finalWindowLongBatch = "+ finalWindowLongBatch);
            }


            for (i=0;i<num;i++){

                currSeconds = batch.getEvents(i).getLastTrade().getSeconds();
                currentTimestamp = stringToTimestamp(formatter.format(new Date(currSeconds * 1000L)),1);
                Timestamp procTimestamp = new Timestamp(System.currentTimeMillis());
                //System.out.println("procTimestamp = "+procTimestamp);
                assert currentTimestamp != null;

                //=========== send data ===========

                value[0][0] = batch.getEvents(i).getSymbol();
                value[0][1] = String.valueOf(batch.getEvents(i).getSecurityType());
                value[0][2] = String.valueOf(currentTimestamp);
                value[0][3] = String.valueOf(batch.getEvents(i).getLastTradePrice());
                value[0][4] = String.valueOf(cnt);     //batch number
                value[0][5] = String.valueOf(i);       //event number inside of current batch
                value[0][6] = String.valueOf(procTimestamp);     //processing ts
                valueToSend[0] = String.join(",", value[0]);

                ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC, 0, currentTimestamp.getTime(), String.valueOf(cnt), valueToSend[0]);

                if (firstSendAfterReceive == 1){
                    startProcTime = System.currentTimeMillis();
                    //System.out.println("startProcTime = "+new Timestamp(startProcTime)+"   cnt: "+cnt);
                    //System.out.println("SENDING: ->  key: "+producerRecord.key()+" value: "+ producerRecord.value());
                    firstSendAfterReceive = 0;
                }
                producer.send(producerRecord, (metadata, exception) -> {
                    if(metadata != null){
                        //successful writes
                        //System.out.println("msgSent: ->  key: "+producerRecord.key()+" value: "+ producerRecord.value());
                    }
                    else{
                        //unsuccessful writes
                        System.out.println("Error Sending Csv Record -> key: " + producerRecord.key()+" value: " + producerRecord.value());
                    }
                });

                //=========== end of send data ===========

                if (currentTimestamp.compareTo(nextWindow)>0) {
                    prev = nextWindow;
                    nextWindow = windowProducingResult(currentTimestamp, nextWindow);
                }


            }

            System.out.println("Sent batch #" + cnt);
            ++cnt;
/*
            if(cnt > 26) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }

 */

        }
        writer.close();
        mustStop = true;
        //challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");
        producer.close();

    }


    //given timestamp lastTs, this method calculates upper bound window (every 5 mins)
    public static Timestamp windowProducingResult(Timestamp lastTs,Timestamp nextWindow){
        long res = nextWindow.getTime();
        while(true){
            res = res + TimeUnit.MINUTES.toMillis(windowLen);
            //System.out.println("res = "+new Timestamp(res));
            if (lastTs.compareTo(new Timestamp(res))<0){
                break;
            }
        }
        return new Timestamp(res);
    }


    public Map<Integer, Long> getBatchSeqId() {
        return batchSeqId;
    }

    public void setBatchSeqId(Map<Integer, Long> batchSeqId) {
        this.batchSeqId = batchSeqId;
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
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }


    public static Map<Tuple2<Integer, String>, Tuple2<Float, Float>> getIntermediateResults() {
        return intermediateResults;
    }

    public static void setIntermediateResults(Map<Tuple2<Integer, String>, Tuple2<Float, Float>> intermediateResults) {
        Producer.intermediateResults = intermediateResults;
    }

    public static Timestamp getFinalWindowLongBatch() {
        return finalWindowLongBatch;
    }

    public static void setFinalWindowLongBatch(Timestamp finalWindowLongBatch) {
        Producer.finalWindowLongBatch = finalWindowLongBatch;
    }

    public static Map<Integer,List<Result>> getFinalResults() {
        return finalResults;
    }

    public static void setFinalResults(Map<Integer,List<Result>> finalResults) {
        Producer.finalResults = finalResults;
    }

}

