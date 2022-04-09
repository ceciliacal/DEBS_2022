package kafka;

import data.Event;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import subscription.challenge.*;
import utils.Config;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Prod2 {

    private static final Integer windowLen = 5; //minutes

    /*
    creates kafka producer
     */
    public static org.apache.kafka.clients.producer.Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }


    /*
    kafka producer streams messages to kafka topic reading csv file
     */
    public static void main(String[] args) throws Exception {

        final org.apache.kafka.clients.producer.Producer<String, String> producer = createProducer();


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
        int i;
        long currSeconds;
        int num;
        long start;
        Timestamp nextWindow = null;
        long next = 0;
        Timestamp windowToFire = null;
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);

        //TODO: porta socket 6666+cnt



        while(true) {
            //ServerSocket ss = new ServerSocket(6667);
            System.out.println("==== cnt: "+cnt);
            Batch batch = challengeClient.nextBatch(newBenchmark);
            num = batch.getEventsCount();

            if (batch.getLast()) { //Stop when we get the last batch
                System.out.println("Received lastbatch, finished!");
                break;
            }


            //=======GESTIONEFINESTRE========
            if (cnt==0){
                start = batch.getEvents(0).getLastTrade().getSeconds() * 1000L;
                next = start;
                next = next + TimeUnit.MINUTES.toMillis(windowLen);
                nextWindow = new Timestamp(next);
                System.out.println("nextWindow = "+nextWindow);
            }


            Timestamp lastTsThisBatch = new Timestamp(batch.getEvents(num-1).getLastTrade().getSeconds() * 1000L);
            System.out.println("lastTsThisBatch = "+lastTsThisBatch);

            if (lastTsThisBatch.compareTo(nextWindow)>0){
                //todo caso =.  chiama funzione che calcola finestra che produce i risultati. se è minore di next window, allora risultati vengono prodotti alla next window
                windowToFire = windowProducingResult(lastTsThisBatch, nextWindow);
                System.out.println("windowToFire= "+windowToFire);
            }
            if (windowToFire.compareTo(nextWindow)<0){
                windowToFire = nextWindow;
            }


            //=======FINE GESTIONEFINESTRE========




            //===========SEND=================

            String[][] value = {new String[6]};
            final String[] valueToSend = new String[1];
            int flag = 0;

            for (i=0;i<num;i++){

                currSeconds = batch.getEvents(i).getLastTrade().getSeconds();
                Timestamp lastTradeTimestamp = stringToTimestamp(formatter.format(new Date(currSeconds * 1000L)),1);
                assert lastTradeTimestamp != null;


                //todo check rispetto a next
                System.out.println("cnt = "+cnt+" lastTradeTimestamp: "+lastTradeTimestamp+" nextWindow: "+nextWindow);
                if (lastTradeTimestamp.compareTo(nextWindow)==0 || lastTradeTimestamp.compareTo(windowToFire)==0){
                    if(flag==0){
                        next = next + TimeUnit.MINUTES.toMillis(windowLen);
                        nextWindow = new Timestamp(next);
                        //calculateIndicators()
                        System.out.println("NELL IF ");
                        System.out.println("lastTradeTimestamp: "+lastTradeTimestamp);
                        System.out.println("nextWindow: "+nextWindow);
                        System.out.println("windowToFire: "+windowToFire);

                        TimeUnit.SECONDS.sleep(2);
                        flag = 1;   //todo: pensa a modo per fare flag

                        /*
                        //ServerSocket ss = new ServerSocket(6667);
                        Socket s = ss.accept();

                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        String str = (String) dis.readUTF();
                        System.out.println("message = "+str);

                        //ss.close();

                         */




                    }
                }

                if (lastTradeTimestamp.compareTo(nextWindow)==0){
                    flag = 0;
                }




                value[0][0] = batch.getEvents(i).getSymbol();
                value[0][1] = String.valueOf(batch.getEvents(i).getSecurityType());
                value[0][2] = String.valueOf(lastTradeTimestamp);
                value[0][3] = String.valueOf(batch.getEvents(i).getLastTradePrice());
                value[0][4] = String.valueOf(cnt);     //batch number
                value[0][5] = String.valueOf(i);       //event number inside of current batch
                valueToSend[0] = String.join(",", value[0]);

                ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, lastTradeTimestamp.getTime(), String.valueOf(cnt), valueToSend[0]);
                System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+ producerRecord.value());

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

            }
            //ss.close();

            //=============ENDSEND===========0

/*
            //process the batch of events we have
            List<Indicator> q1Results = null;
            q1Results = calculateIndicators(batch, cnt, producer);


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

 */
            System.out.println("Processed batch #" + cnt);
            ++cnt;

            //todo: prima era 100
            if(cnt > 72) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
        }

        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");




        producer.close();

    }

    public static Timestamp windowProducingResult(Timestamp lastTs,Timestamp nextWindow){
        long res = nextWindow.getTime();
        while(true){
            res = res + TimeUnit.MINUTES.toMillis(windowLen);
            System.out.println("res = "+new Timestamp(res));
            if (lastTs.compareTo(new Timestamp(res))<0){
                break;
            }
        }
        return new Timestamp(res);
    }

    public static List<Indicator> calculateIndicators(Batch batch, int cnt, Producer<String, String> producer) throws IOException, InterruptedException {

        /*
        int i;
        Long seconds;
        int num = batch.getEventsCount();
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);
        String[][] value = {new String[6]};
        final String[] valueToSend = new String[1];

        //TODO: porta socket 6666+cnt
        System.out.println("==== cnt: "+cnt);
        ServerSocket ss = new ServerSocket(6667);

        if (batch == null){
            return new ArrayList<>();
        }

        //num
        for (i=0;i<num;i++){

            seconds = batch.getEvents(i).getLastTrade().getSeconds();
            Timestamp lastTradeTimestamp = Event.stringToTimestamp(formatter.format(new Date(seconds * 1000L)),1);
            assert lastTradeTimestamp != null;

            value[0][0] = batch.getEvents(i).getSymbol();
            value[0][1] = String.valueOf(batch.getEvents(i).getSecurityType());
            value[0][2] = String.valueOf(lastTradeTimestamp);
            value[0][3] = String.valueOf(batch.getEvents(i).getLastTradePrice());
            value[0][4] = String.valueOf(cnt);     //batch number
            value[0][5] = String.valueOf(i);       //event number inside of current batch
            valueToSend[0] = String.join(",", value[0]);

            ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, lastTradeTimestamp.getTime(), String.valueOf(cnt), valueToSend[0]);
            System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+ producerRecord.value());

            producer.send(producerRecord, (metadata, exception) -> {
                if(metadata != null){
                    //successful writes
                    System.out.println("msgSent: ->  key: "+producerRecord.key()+" value: "+ producerRecord.value());
                }
                else{
                    //unsuccessful writes
                    System.out.println("Error Sending Csv Record -> key: " + producerRecord.key()+" value: " + producerRecord.value());
                }
            });

        }



        //ServerSocket ss = new ServerSocket(6667);
        Socket s = ss.accept();

        DataInputStream dis = new DataInputStream(s.getInputStream());
        String str = (String) dis.readUTF();
        System.out.println("message = "+str);






        ss.close();

        //TimeUnit.SECONDS.sleep(10);

         */
        return new ArrayList<>();

    }


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



    /*
        //before processing ============= INVIO DEI DATI FINO AL BATCH CORRENTE A KAFKA =============
        Stream<String> fileStream = Files.lines(Paths.get(datasetPath+".csv"));


        fileStream.skip(countFileLine.get()).forEach(line -> {

            countFileLine.incrementAndGet();


            String[] lineFields = line.split(",");

            Timestamp lastUpdateTs = createTimestamp(lineFields[2], lineFields[3]);

            //creating producer record (value) to send. it only contains data (from csv)actually useful for query's result
            value[0][0] = lineFields[0];                //sec type
            value[0][1] = lineFields[1];                //sec type
            value[0][2]= lastUpdateTs.toString();       //ts for last received update
            value[0][3]= lineFields[21];                //last trade price

            valueToSend[0] = String.join(",", value[0]);

            ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, lastUpdateTs.getTime(), lineFields[0], valueToSend[0]);
            System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+producerRecord.value());

            producer.send(producerRecord, (metadata, exception) -> {
                if(metadata != null){
                    //successful writes
                    System.out.println("msgSent: ->  key: "+producerRecord.key()+" value: "+ producerRecord.value());
                }
                else{
                    //unsuccessful writes
                    System.out.println("Error Sending Csv Record -> key: " + producerRecord.key()+" value: " + producerRecord.value());
                }
            });

        });

        System.out.println("fine FOREACH, countFileLine = "+countFileLine.get());
        fileStream.close();

        //before processing ============= FINE INVIO DEI DATI =============

         */

