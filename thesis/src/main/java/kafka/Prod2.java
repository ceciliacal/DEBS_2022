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
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Prod2 {

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
        while(true) {
            Batch batch = challengeClient.nextBatch(newBenchmark);
            if (batch.getLast()) { //Stop when we get the last batch
                System.out.println("Received lastbatch, finished!");
                break;
            }

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

/*
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
            if(cnt > 1) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
        }

        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");


        producer.close();

    }

    public static List<Indicator> calculateIndicators(Batch batch, int cnt, Producer<String, String> producer) throws IOException, InterruptedException {

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

        return new ArrayList<>();
    }



    public static List<CrossoverEvent> calculateCrossoverEvents(Batch batch) {
        //TODO: improve this implementation

        return new ArrayList<>();
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

