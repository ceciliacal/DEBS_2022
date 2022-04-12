package kafka;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import scala.Tuple2;
import subscription.challenge.*;
import utils.Config;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Producer {

    private static final Integer windowLen = 5; //minutes
    private static Map<Integer,Long> batchSeqId;       //to retrieve each bach when sending aggregated results
    private static Map<Tuple2<Integer, String>, Tuple2<Float, Float>> intermediateResults;      //K: #batch+symbol V:ema38+ema100
    private static Timestamp finalWindowLongBatch = null ;
    private static Map<Integer,List<Result>> finalResults;

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

    /*
                kafka producer streams messages to kafka topic reading csv file
                 */
    public static void main(String[] args) throws Exception {

        final org.apache.kafka.clients.producer.Producer<String, String> producer = createProducer();
        batchSeqId = new HashMap<>();
        intermediateResults = new HashMap<>();
        finalResults = new HashMap<>();
        int longBatch = -1;

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
        SimpleDateFormat formatter = new SimpleDateFormat(Config.pattern);

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
                break;  //todo ultimo batch fallo qui e sposta questo if sotto
            }


            //======= windows setup ========
            if (cnt==0){
                start = batch.getEvents(0).getLastTrade().getSeconds() * 1000L;
                next = start + TimeUnit.MINUTES.toMillis(windowLen);
                nextWindow = new Timestamp(next);
                System.out.println("nextWindow = "+nextWindow);
            }
            //==== end of windows setup =====


            //=========== send data ===========

            String[][] value = {new String[6]};
            final String[] valueToSend = new String[1];

            long lastTsSeconds = batch.getEvents(num-1).getLastTrade().getSeconds();
            Timestamp lastTsBatch = stringToTimestamp(formatter.format(new Date(lastTsSeconds * 1000L)),1);
            long startTsSeconds = batch.getEvents(0).getLastTrade().getSeconds();
            Timestamp startTsBatch = stringToTimestamp(formatter.format(new Date(startTsSeconds * 1000L)),1);

            System.out.println("startTsBatch = "+startTsBatch);
            System.out.println("lastTsBatch = "+lastTsBatch);

            if (lastTsSeconds - startTsSeconds > TimeUnit.MINUTES.toSeconds(windowLen)){
                System.out.println("ts sta in piu finestre");
                longBatch=cnt;
                setFinalWindowLongBatch(windowProducingResult(lastTsBatch,nextWindow));

            }
            System.out.println("whenResult = "+ finalWindowLongBatch);


            for (i=0;i<num;i++){

                currSeconds = batch.getEvents(i).getLastTrade().getSeconds();
                Timestamp lastTradeTimestamp = stringToTimestamp(formatter.format(new Date(currSeconds * 1000L)),1);
                assert lastTradeTimestamp != null;

                //System.out.println("cnt = "+cnt+" lastTradeTimestamp: "+lastTradeTimestamp+" nextWindow: "+nextWindow);
                value[0][0] = batch.getEvents(i).getSymbol();
                value[0][1] = String.valueOf(batch.getEvents(i).getSecurityType());
                value[0][2] = String.valueOf(lastTradeTimestamp);
                value[0][3] = String.valueOf(batch.getEvents(i).getLastTradePrice());
                value[0][4] = String.valueOf(cnt);     //batch number
                value[0][5] = String.valueOf(i);       //event number inside of current batch
                valueToSend[0] = String.join(",", value[0]);

                ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, lastTradeTimestamp.getTime(), String.valueOf(cnt), valueToSend[0]);
                //System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+ producerRecord.value());

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


                if (lastTradeTimestamp.compareTo(nextWindow)>0){

                    ServerSocket ss = new ServerSocket(6667);
                    Timestamp prev = nextWindow;
                    next = next + TimeUnit.MINUTES.toMillis(windowLen);
                    nextWindow = new Timestamp(next);

                    System.out.println("NELL IF ");
                    System.out.println("lastTradeTimestamp: "+lastTradeTimestamp);
                    System.out.println("nextWindow: "+nextWindow);

                    Socket s = ss.accept();

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    ss.close();

                    byte[] bytes = dis.readAllBytes();
                    String str = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("message = "+str);


                    if(finalWindowLongBatch!=null){
                        //System.out.println("WHEN RES È DIVERSO NULL! "+cnt);
                        //ci entro solo quando il batch sta in piu finestre ->
                        //System.out.println("prev = "+prev);

                        if (finalWindowLongBatch.compareTo(prev)>0){
                            System.out.println("NON HO FINITO "+cnt);
                            putIntoMap(str, longBatch);
                        }
                        //System.out.println("intermediateResults = "+intermediateResults);

                        if (lastTradeTimestamp.compareTo(finalWindowLongBatch)>0){
                            System.out.println("HO FINITO!!");
                            putIntoMap(str,longBatch);    //che hanno batch 0 !!!!!!!!
                            //System.out.println("intermediateResults finito= "+intermediateResults);
                        }
                    }
                    if(cnt!=longBatch){
                        finalWindowLongBatch = null;
                        List<Integer> batchesInCurrentWindow = calculateResults(str, longBatch);

                        for(i=0;i<batchesInCurrentWindow.size();i++) {
                            System.out.println("prima calcIndic - i: "+i);
                            List<Indicator> indicatorsList = calculateIndicators(batchesInCurrentWindow.get(i));
                            //System.out.println("indicatorsList = "+indicatorsList);
                            ResultQ1 q1Result = ResultQ1.newBuilder()
                                    .setBenchmarkId(newBenchmark.getId()) //set the benchmark id
                                    .setBatchSeqId(batchSeqId.get(cnt)) //set the sequence number
                                    .addAllIndicators(indicatorsList)
                                    .build();
                            challengeClient.resultQ1(q1Result);

                            //todo calculate query2
                            System.out.println("---fineITERAZ---");
                        }

                    }
                    //TODO: prendi ultimo ts dell ultimo batch e manda dato x chiudere

                }

            }

            //=========== end of send data ===========

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
            //longBatch = -1;   //big batch just finished
            System.out.println("Processed batch #" + cnt);
            ++cnt;

            //todo: prima era 100
            if(cnt > 26) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
        }

        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");
        producer.close();

    }

    public static void putIntoMap(String str, int batchGrande) {

        String[] lines = str.split("\n");
        for (String line : lines) {
            String[] values = line.split(";");
            if (Integer.valueOf(values[1])==batchGrande){
                intermediateResults.put(new Tuple2<>(Integer.valueOf(values[1]),values[2]), new Tuple2<>(Float.valueOf(values[3]),Float.valueOf(values[4])));
            }
        }
    }

    //serve a popolare mappa finalResults
    public static List<Integer> calculateResults(String str, int longBatch) throws IOException, InterruptedException {
        System.out.println("STO IN CALCULATERES!!");
        Result res;
        List<Integer> batchesInCurrentWindow = new ArrayList<>();
        List<Timestamp> buysTs = null;
        List<Timestamp> sellsTs = null;
        finalResults = new HashMap<>();     //per ogni finestra lo riazzero

        String[] lines = str.split("\n");
        for (String line : lines) {
            String[] values = line.split(";");

            int currBatch = Integer.valueOf(values[1]);
            if (!batchesInCurrentWindow.contains(currBatch)){
                batchesInCurrentWindow.add(currBatch);
            }
            //System.out.println("currBatch= "+currBatch);

            if (!values[5].equals("null")) {
                //parse lista ts
                buysTs = createTimestampsList(values[5]);
            }
            else if(!values[6].equals("null")){
                sellsTs = createTimestampsList(values[6]);
            }

            if(currBatch==longBatch) {  //se il batch della riga che sto analizzando coincide con longBatch
                //prendo dalla mappa intermediate la key <longBatch,simboloCorrente> e aggiungo i relativi ema alla lista di Result per popolare finalResults
                //System.out.println("sto in: currBatch==longBatch");
                Tuple2<Float, Float> emas = intermediateResults.get(new Tuple2<>(longBatch, values[2]));
                res = new Result(values[2], emas._1, emas._2, null, null);
            } else {
                res = new Result(values[2], Float.parseFloat(values[3]), Float.parseFloat(values[4]), null,null);
            }

            if (buysTs!=null){
                res.setBuys(buysTs);
                //System.out.println("res BUY: "+res);
            } else if (sellsTs!=null){
                res.setSells(sellsTs);
            }

            if(!finalResults.containsKey(currBatch)){
                List<Result> resList = new ArrayList<>();
                resList.add(res);
                finalResults.put(currBatch,resList);
                //System.out.println("NOT");
            } else {
                List<Result> resList = finalResults.get(currBatch);
                resList.add(res);
                finalResults.put(currBatch,resList);
                //System.out.println("YES");
            }
            //ho analizzato singola linea
            buysTs = null;
            sellsTs = null;
        } //ho analizzato tutte le linee

        //System.out.println("finalResults = "+finalResults);

        return batchesInCurrentWindow;
    }

    public static List<Indicator> calculateIndicators(int i) throws IOException, InterruptedException {

        //System.out.println("STO IN CALCULATE INDICATORS!!!!!!!!!!! + i="+i);
        List<Indicator> indicatorsList = new ArrayList<>();

        List<Result> resList = finalResults.get(i);
        //System.out.println("finalResults.: "+finalResults);
        //System.out.println("i: "+i);
        //System.out.println("resList: "+resList);
        resList.stream().forEach(res -> {
            //indicator
            Indicator.Builder ind = Indicator.newBuilder();
            ind.setSymbol(res.getSymbol());
            ind.setEma38(Float.valueOf(res.getEma38()));
            ind.setEma100(Float.valueOf(res.getEma100()));
            //add list indicator
            indicatorsList.add(ind.build());
            //seqId batch
            //batchSeqId.get(cnt)

        } );
        //invio questi risultati
        //qui ho risultati del batch i-esimo -> ho list<Indicator> del batch i-esimo


    //System.out.println("indicatorsList: "+indicatorsList);


        return indicatorsList;

    }

    public static List<Timestamp> createTimestampsList(String str){
        //String str = "[2021-11-08 08:10:00.0]";

        List<Timestamp> list = new ArrayList<>();
        String[] line = str.split(",");
        int len = line.length;
        //System.out.println("len = "+len);
        if (len>1){
            line[0] = line[0].substring(1);
            int lastStrlen = line[len-1].length();
            line[len-1] = line[len-1].substring(0,lastStrlen-1);
        } else {
            int lastStrlen = line[0].length();
            //System.out.println("lastStrlen = "+lastStrlen);
            line[0]  = line[0].substring(1,lastStrlen-1);
        }
        for (String s : line) {
            //TRASFORMO IN LISTA DI TS
            Timestamp ts = Producer.stringToTimestamp(s, 0);
            list.add(ts);
        }
        //System.out.println("list = "+list);
        return list;
    }

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

    public static List<CrossoverEvent> calculateCrossoverEvents(Batch batch) {
        //TODO: improve this implementation

        return new ArrayList<>();
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

