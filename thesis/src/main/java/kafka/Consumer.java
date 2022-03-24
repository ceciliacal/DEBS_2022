package kafka;

import flink.query1.Query1;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import subscription.challenge.Indicator;
import utils.Config;
import data.Event;

import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class Consumer {

    protected static List<Event> myBatch = null;

    public static List<Indicator> startConsumer(List<Event> batch) throws Exception {

        FlinkKafkaConsumer<String> consumer = createConsumer();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)));
        StreamExecutionEnvironment env = createEnviroment();

        myBatch = batch;

        //todo: dopo aver capito a che serve csv, SE SERVE fare multisource (sia batch sia kafka cosi unico stream e non uso for nel main)

        DataStream<Event> eventDataStream = env.addSource(consumer)
                .map(new MapFunctionEvent());

        Query1.runQuery1(eventDataStream);
        env.execute("debsTest");
        return null;
    }


    public static void main(String[] args) throws Exception {

        FlinkKafkaConsumer<String> consumer = createConsumer();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMinutes(1)));
        StreamExecutionEnvironment env = createEnviroment();

        //creo lo stream di tipo "Ship" andando a splittare le righe
        //che vengono lette dal topic di kafka da parte del consumer con MyMapFunction


        DataStreamSink<String> stream = env.addSource(consumer)
                .print();

        env.execute("debsTest");

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


}