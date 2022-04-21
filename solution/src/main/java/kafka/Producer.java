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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
    public static Benchmark newBenchmark = null;


    public static Benchmark getNewBenchmark() {
        return newBenchmark;
    }

    public static void setNewBenchmark(Benchmark newBenchmark) {
        Producer.newBenchmark = newBenchmark;
    }

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
        int longBatch = -1;


        //int port = Integer.parseInt(args[0]);
        int port = 6668;
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
                .setToken("jkninvezfgvcwexklizimkoonqmudupq") //go to: https://challenge.msrg.in.tum.de/profile/
                .setBenchmarkType("evaluation") //Benchmark Type for evaluation
                //.setBenchmarkType("test") //Benchmark Type for testing
                .build();

        //Create a new Benchmark
        Benchmark newBenchmark = challengeClient.createNewBenchmark(bc);

        //Start the benchmark
        challengeClient.startBenchmark(newBenchmark);
        System.out.println("newBenchmark = "+getNewBenchmark());

        PrintWriter writer = new PrintWriter("/tmp/procTimes.txt", "UTF-8");
        //PrintWriter writer = new PrintWriter("procTimes.txt", "UTF-8");

        //Process the events
        int cnt = 0;
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
                Timestamp currentTimestamp = stringToTimestamp(formatter.format(new Date(currSeconds * 1000L)),1);
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



                //=========== receive results ===========
                if (currentTimestamp.compareTo(nextWindow)>0){

                    ServerSocket ss = new ServerSocket(port);
                    Timestamp prev = nextWindow;
                    nextWindow = windowProducingResult(currentTimestamp, nextWindow);

                    //System.out.println("Window has fired. Receiving data and updating new window's timestamp...");
                    //System.out.println("currentTimestamp: "+currentTimestamp);
                    //System.out.println("nextWindow: "+nextWindow);

                    //get results through socket. we get one string per window containing the results of all batches falling in that window
                    Socket s = ss.accept();

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    ss.close();

                    //differenza fra tempo in cui ricevo risultato ogni 5 minuti (quanto ci mette a processare una finestra)
                    // e quando questa finestra è iniziata. Inizia in due casi: o se i=0 e cnt=0, oppure al primo invio dopo la ricezione.
                    resultsProcTime = System.currentTimeMillis();
                    //System.out.println("resultsProcTime = "+new Timestamp(resultsProcTime));
                    timeToProcessAWindow = resultsProcTime - startProcTime;
                    System.out.println("timeToProcessAWindow = "+ timeToProcessAWindow/1000L);
                    writer.println(String.valueOf(timeToProcessAWindow/1000L));

                    byte[] bytes = dis.readAllBytes();
                    String str = new String(bytes, StandardCharsets.UTF_8);
                    //System.out.println("message = "+str);


                    if(finalWindowLongBatch!=null){
                        //in here only when batch size is longer than 5 mins
                        if (finalWindowLongBatch.compareTo(prev)>0){
                            //System.out.println("Long batch "+cnt+" isn't over yet.");
                            putIntoMap(str, longBatch);
                        }
                        //System.out.println("intermediateResults = "+intermediateResults);

                        if (currentTimestamp.compareTo(finalWindowLongBatch)>0){
                            //System.out.println("Long batch "+cnt+" is over.");
                            putIntoMap(str,longBatch);    //che hanno batch 0 !!!!!!!!
                            //System.out.println("intermediateResults finito= "+intermediateResults);
                        }
                    }
                    if(cnt!=longBatch){
                        finalWindowLongBatch = null;
                        List<Integer> batchesInCurrentWindow = calculateResults(str, longBatch);

                        for(i=0;i<batchesInCurrentWindow.size();i++) {
                            //sending query1 results
                            List<Indicator> indicatorsList = calculateIndicators(batchesInCurrentWindow.get(i));
                            //System.out.println("batchesInCurrentWindow.get(i) = "+batchesInCurrentWindow.get(i));
                            ResultQ1 q1Result = ResultQ1.newBuilder()
                                    .setBenchmarkId(newBenchmark.getId()) //set the benchmark id
                                    .setBatchSeqId(batchSeqId.get(batchesInCurrentWindow.get(i))) //set the sequence number
                                    .addAllIndicators(indicatorsList)
                                    .build();
                            challengeClient.resultQ1(q1Result);

                            //sending query2 results
                            List<CrossoverEvent> crossoverEventList = calculateCrossoverEvents(batchesInCurrentWindow.get(i));
                            ResultQ2 q2Result = ResultQ2.newBuilder()
                                    .setBenchmarkId(newBenchmark.getId()) //set the benchmark id
                                    .setBatchSeqId(batchSeqId.get(batchesInCurrentWindow.get(i))) //set the sequence number
                                    .addAllCrossoverEvents(crossoverEventList)
                                    .build();

                            challengeClient.resultQ2(q2Result);

                        }

                    }

                    System.out.println("sto uscendo dai 5 min!!!");
                    firstSendAfterReceive = 1;
                }

                //=========== end of receive results ===========




            }

            System.out.println("Processed batch #" + cnt);
            ++cnt;

/*
            if(cnt > 26) { //for testing you can stop early, in an evaluation run, run until getLast() is True.
                break;
            }
 */



        }
        writer.close();
        challengeClient.endBenchmark(newBenchmark);
        System.out.println("ended Benchmark");
        producer.close();

    }

    //putting results from batch longer than one window inside of "intermediateResults" map to collect them later on
    public static void putIntoMap(String str, int longBatch) {

        String[] lines = str.split("\n");
        for (String line : lines) {
            String[] values = line.split(";");
            if (Integer.valueOf(values[1])==longBatch){
                intermediateResults.put(new Tuple2<>(Integer.valueOf(values[1]),values[2]), new Tuple2<>(Float.valueOf(values[3]),Float.valueOf(values[4])));
            }
        }
    }

    //populates finalResults map
    public static List<Integer> calculateResults(String str, int longBatch) throws IOException, InterruptedException {
        //System.out.println("Collecting results!");
        Result res;
        List<Integer> batchesInCurrentWindow = new ArrayList<>();
        List<Timestamp> buysTs = null;
        List<Timestamp> sellsTs = null;
        finalResults = new HashMap<>();     //every window has a new map

        String[] lines = str.split("\n");       //splitting the whole string (contains ALL results, separated from "\n")
        for (String line : lines) {
            String[] values = line.split(";");  //splitting each field in one single line

            int currBatch = Integer.valueOf(values[1]);
            if (!batchesInCurrentWindow.contains(currBatch)){
                batchesInCurrentWindow.add(currBatch);
            }
            //System.out.println("currBatch= "+currBatch);

            //retrieving crossovers ts lists (if any)
            if (!values[5].equals("null")) {
                //parse ts list
                buysTs = createTimestampsList(values[5]);
            }
            else if(!values[6].equals("null")){
                sellsTs = createTimestampsList(values[6]);
            }

            //if current line's batch equals longBatch get from intermediateResults map
            //the key <longBatch,currentSymbol> ad add its emas values to a Result object list in order to populate finalResults map.
            if(currBatch==longBatch) {
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
                //System.out.println("NO");
            } else {
                List<Result> resList = finalResults.get(currBatch);
                resList.add(res);
                finalResults.put(currBatch,resList);
                //System.out.println("YES");
            }
            //finish analyzing single line
            buysTs = null;
            sellsTs = null;
        } //finish analyzing all lines

        //System.out.println("finalResults = "+finalResults);
        return batchesInCurrentWindow;
    }

    public static List<Indicator> calculateIndicators(int i) {

        List<Indicator> indicatorsList = new ArrayList<>();
        List<Result> resList = finalResults.get(i);

        resList.stream().forEach(res -> {
            //indicator
            Indicator.Builder ind = Indicator.newBuilder();
            ind.setSymbol(res.getSymbol());
            ind.setEma38(Float.valueOf(res.getEma38()));
            ind.setEma100(Float.valueOf(res.getEma100()));
            //add list indicator
            indicatorsList.add(ind.build());


        } );

        //here we get list<Indicator> of #i batch

        return indicatorsList;

    }

    public static List<Timestamp> createTimestampsList(String str){

        List<Timestamp> list = new ArrayList<>();
        String[] line = str.split(",");
        int len = line.length;

        if (len>1){
            line[0] = line[0].substring(1);
            int lastStrlen = line[len-1].length();
            line[len-1] = line[len-1].substring(0,lastStrlen-1);
        } else {
            int lastStrlen = line[0].length();
            line[0]  = line[0].substring(1,lastStrlen-1);
        }

        for (String s : line) {
            //trasform string array in timestamp list
            Timestamp ts = Producer.stringToTimestamp(s, 0);
            list.add(ts);
        }

        return list;
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

    public static List<CrossoverEvent> calculateCrossoverEvents(int i) {
        //System.out.println("STO IN CROSSOVERS!!!!!!!!!!! + i="+i);

        List<CrossoverEvent> crossoverEventList = new ArrayList<>();

        List<Result> resList = finalResults.get(i);
        resList.stream().forEach(res -> {
            CrossoverEvent.Builder cross = CrossoverEvent.newBuilder();
            cross.setSymbol(res.getSymbol());
            if (res.getBuys()!=null){  //if list is null that symbol has no crossovers
                //if it does, we put each one of them inside CrossoverEvent through setTs

                for(Timestamp ts: res.getBuys()){  //set buys (at maximum, they're 3)
                    com.google.protobuf.Timestamp timestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(ts.getTime()).build();
                    cross.setTs(timestamp);
                }
            }
            if (res.getSells()!=null){
                for(Timestamp ts: res.getSells()){  //set sells (at maximum, they're 3)
                    com.google.protobuf.Timestamp timestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(ts.getTime()).build();
                    cross.setTs(timestamp);
                }
            }

            crossoverEventList.add(cross.build());
        });
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

