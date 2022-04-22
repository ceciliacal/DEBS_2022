package flink;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.Config;
import data.Event;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;


public class Queries {

    public static void runQueries(DataStream<Event> stream, int port){

        KeyedStream<Event, String> keyedStream = stream
                .keyBy(Event::getSymbol);

        KafkaSink<String> sink = KafkaSink.<String>builder().setBootstrapServers(Config.KAFKA_BROKERS).setKafkaProducerConfig(getFlinkPropAsProducer()).setRecordSerializer(KafkaRecordSerializationSchema.builder().setTopic(Config.TOPIC_RES).setValueSerializationSchema(new SimpleStringSchema()).build()).setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE).build();

        keyedStream
                .window(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction())
                //.setParallelism(3)
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(Config.windowLen)))
                .process(new ProcessAllWindowFunction<FinalOutput, String, TimeWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<FinalOutput, String, TimeWindow>.Context context, Iterable<FinalOutput> elements, Collector<String> out) throws Exception {

                        System.out.println("proc-time = "+new Timestamp(System.currentTimeMillis()));

                        System.out.println("Firing window: "+new Date(context.window().getStart()));
                        FinalOutput res = elements.iterator().next();  //first element in iterator

                        int i = 0;

                        String stringToSend = "0;"+
                                res.getBatch()+";"+
                                res.getSymbol()+";"+
                                res.getSymbol_WindowEma38().get(res.getSymbol())._2+";"+
                                res.getSymbol_WindowEma100().get(res.getSymbol())._2+";"+
                                res.getSymbol_buyCrossovers().get(res.getSymbol())+";"+
                                res.getSymbol_sellCrossovers().get(res.getSymbol())+","
                                ;

                        for (FinalOutput element : elements) {
                            //System.out.println("element: "+element);
                            if (i==0){
                                stringToSend = stringToSend;
                            } else{

                                stringToSend = stringToSend +
                                        i+";"+
                                        element.getBatch()+";"+
                                        element.getSymbol()+";"+
                                        element.getSymbol_WindowEma38().get(element.getSymbol())._2+";"+
                                        element.getSymbol_WindowEma100().get(element.getSymbol())._2+";"+
                                        element.getSymbol_buyCrossovers().get(element.getSymbol())+";"+
                                        element.getSymbol_sellCrossovers().get(element.getSymbol())+","
                                        ;

                            }

                            //System.out.println(new Date(context.window().getStart()) + " " + element);
                            i++;
                        }

                        out.collect(stringToSend);

                    }
                })

                .sinkTo(sink);
    }


    //metodo che crea proprietà per creare sink verso kafka
    public static Properties getFlinkPropAsProducer(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Config.KAFKA_BROKERS);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,Config.CLIENT_ID);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");

        return properties;

    }
}
