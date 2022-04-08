package kafka;

import flink.query1.MapFunctionEvent;
import flink.query1.Query1;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.source.TimestampedInputSplit;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import scala.Tuple2;
import subscription.challenge.Indicator;
import utils.Config;
import data.Event;

import javax.annotation.Nullable;
import java.io.DataOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class Consumer {

    public static Map<Integer, Tuple2<Timestamp, Timestamp>> startEndTsPerBatch;
    private static long startTime;
    //private static Map<Tuple2<String,Integer>,Float> myEma38;   //K: <symbol,countWindow> - V: ema
    public static Timestamp endBatchTs;

/*
    public static List<Indicator> startConsumer(Map<String, Timestamp> subSymbols) throws Exception {

        subscribedSymbols = subSymbols;

        FlinkKafkaConsumer<String> consumer = createConsumer();
        StreamExecutionEnvironment env = createEnviroment();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(30)));

        /*
        DataStream<Event> eventDataStream = env.addSource(consumer)
                .map(new MapFunctionEvent());
        Query1.runQuery1(eventDataStream);

        env.execute("debsTest");



        return null;

    }

 */

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Consumer.startTime = startTime;
    }


    public static FlinkKafkaConsumer<String> createConsumer() throws Exception {
        // creazione properties
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerGroup");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // creazione consumer usando le properties
        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<>(Config.TOPIC1, new SimpleStringSchema(), props);

        System.out.println("---creato consumer--");
        return myConsumer;

    }

    public static StreamExecutionEnvironment createEnviroment(){
        System.out.println("--sto in create env--");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        return env;
    }


    public static void main(String[] args) throws Exception {

        FlinkKafkaConsumer<String> consumer = createConsumer();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMillis(1)));
        StreamExecutionEnvironment env = createEnviroment();

        //myEma38 = new HashMap<>();


        DataStream<Event> stream = env.addSource(consumer)
                .map(new MapFunctionEvent());

        Query1.runQuery1(stream);

                /*.map(new MapFunction<String, String>() {
                    @Override
                    public String map(String value) throws Exception {

                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("value : "+value);
                        Socket s = new Socket("localhost",6667);

                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("caccaaa ciao");
                        dout.flush();
                        dout.close();
                        s.close();
                        return value;
                    }
                })

                 */
        //.print();

        env.execute("debsTest");

    }




}