package kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
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
import utils.Event;

import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class Consumer2 {

    private List<String> subSymbols;

    public static List<Indicator> startConsumer(List<String> symbols) throws Exception {

        FlinkKafkaConsumer<String> consumer = createConsumer();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMinutes(Config.windowSize)));
        StreamExecutionEnvironment env = createEnviroment();

        DataStream<Event> eventDataStream = env.addSource(consumer)
                .map(new MapFunction<String, Event>() {
                    @Override
                    public Event map(String value) throws Exception {
                        String line[] = value.split(",");
                        Event event = new Event(line[0], 0,line[1], Double.parseDouble(line[2]), Long.parseLong(line[3]));
                        System.out.println("event = "+event.getSymbol()+", "+event.getBatch()+", "+event.getSecType()+", "+event.getLastTradePrice()+", "+event.getLastTradeTime());
                        return event;
                    }
                })
                ;

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