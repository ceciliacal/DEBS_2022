package kafka;

import flink.MapFunctionEvent;
import flink.Queries;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import utils.Config;
import data.Event;

import java.time.Duration;
import java.util.Properties;


public class Consumer {

    private static long startTime;

    //creating kafka consumer to listen for data in kafka broker
    public static void main(String[] args) throws Exception {
        
        int port = Integer.parseInt(args[0]);
        System.out.println("in CONSUMER: port= "+port);


        FlinkKafkaConsumer<String> consumer = createConsumer();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMillis(1)));
        StreamExecutionEnvironment env = createEnviroment();

        //mapping data from source into datastream of Events
        DataStream<Event> stream = env.addSource(consumer)
                .map(new MapFunctionEvent());

        //start queries calculation
        Queries.runQueries(stream,port);
        env.execute("debsTest");

    }

    public static FlinkKafkaConsumer<String> createConsumer() throws Exception {
        //properties creation
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerGroup");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        //consumer creation
        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<>(Config.TOPIC, new SimpleStringSchema(), props);

        System.out.println("---consumer created---");
        return myConsumer;

    }

    public static StreamExecutionEnvironment createEnviroment(){
        System.out.println("--enviroment created---");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        return env;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Consumer.startTime = startTime;
    }

}