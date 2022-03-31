package kafka;

import flink.query1.MapFunctionEvent;
import flink.query1.Query1;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.source.TimestampedInputSplit;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import subscription.challenge.Indicator;
import utils.Config;
import data.Event;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

/*
        io dovrei leggere dati da env.addSource e poi quando faccio map e li trasformo in Event so che per quel batch
        sono eventInBatch. Mi calcolo quindi ema primo batch fino al 8.15, poi quando passo al secondo batch devo
        sicuramente mantenere dati calcolati per primo batch

        cioè io è come se ho subito tutta la sorgente di dati e devo leggere fino alla riga con ts max sul csv (ts max che recupero
        dai subsymbols) e poi quando ricevo batch dopo rileggo fino al punto max ecc.. -> quindi devo settare sia event time
         */

public class TestClass {

    public static Map<String, Timestamp> subscribedSymbols = null;
    public static Integer countBatch;
    public static Timestamp startBatchTs;
    public static Timestamp endBatchTs;



    public static void start0(Map<String, Timestamp> subSymbols, int numBatch){
        subscribedSymbols = subSymbols;

        if (numBatch==0){

        }

    }
    public static List<Indicator> start(Map<String, Timestamp> subSymbols, int cnt, Timestamp start) throws Exception {

        subscribedSymbols = subSymbols;
        countBatch = cnt;
        startBatchTs = start;
        endBatchTs = Collections.min(subscribedSymbols.values());
        System.out.println("endBatchTs = "+endBatchTs);
        System.out.println("startBatchTs = "+startBatchTs);


        StreamExecutionEnvironment env = createEnviroment();

        DataStream<Event> inputStream = env.addSource(new MySourceFunction(Config.datasetPath+".csv"))
                .assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(30)));
                //.setParallelism(1)

        /*
        inputStream.keyBy(event -> event.getSymbol())
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .process(new ProcessWindowFunction<Event, Object, String, TimeWindow>() {
                            @Override
                            public void process(String s, ProcessWindowFunction<Event, Object, String, TimeWindow>.Context context, Iterable<Event> elements, Collector<Object> out) throws Exception {
                                Long windowStart = context.window().getStart();
                                Date date = new Date();
                                date.setTime(windowStart);
                                System.out.println("START WINDOW: "+date+" event = "+elements.iterator().next().getSymbol()+" "+elements.iterator().next().getTimestamp());
                            }
                });
                
         */
                
        //todo qui un if se query è 1 o 2

        /*
        DataStream<Event> stream = inputStream
                .map(new MapFunctionEvent());

         */

        inputStream.print();


        env.execute("debsTest2");
        return null;
    }


    public static StreamExecutionEnvironment createEnviroment(){
        System.out.println("--sto in create env--");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        return env;
    }




}