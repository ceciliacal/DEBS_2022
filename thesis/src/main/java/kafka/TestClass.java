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
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import subscription.challenge.Indicator;
import utils.Config;
import data.Event;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
        io dovrei leggere dati da env.addSource e poi quando faccio map e li trasformo in Event so che per quel batch
        sono eventInBatch. Mi calcolo quindi ema primo batch fino al 8.15, poi quando passo al secondo batch devo
        sicuramente mantenere dati calcolati per primo batch

        cioè io è come se ho subito tutta la sorgente di dati e devo leggere fino alla riga con ts max sul csv (ts max che recupero
        dai subsymbols) e poi quando ricevo batch dopo rileggo fino al punto max ecc.. -> quindi devo settare sia event time
         */

public class TestClass {

    public static Map<String, Timestamp> subscribedSymbols = null;

    public static List<Indicator> start(Map<String, Timestamp> subSymbols) throws Exception {

        subscribedSymbols = subSymbols;
        StreamExecutionEnvironment env = createEnviroment();

        DataStream<String> inputStream = env.addSource(new MySourceFunction(Config.datasetPath+".csv"))
                //.setParallelism(1)
                ;

        DataStream<Event> stream = inputStream
                .map(new MapFunctionEvent());

                stream.print();


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