package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class Producer2 {

    /*
    creates kafka producer
     */
    public static Producer<String, String> createProducer() {
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
    public static void PublishMessages() throws IOException {

        AtomicInteger count = new AtomicInteger(); //counts line (currently reading)
        List<String> csvTimestamps = new ArrayList<>();

        final Producer<String, String> producer = createProducer();
        long time = System.currentTimeMillis();
        System.out.println("PROVA");

        //invio dei messaggi
        ProducerRecord<String, String> myMessage = new ProducerRecord<>( Config.TOPIC1, "hello");

        //invio dei messaggi
        ProducerRecord<String, String> CsvRecord = new ProducerRecord<>( Config.TOPIC1, 0, "KEY", "VALLL");

        //invio record
        System.out.println("dopo MYMESSAGE");
        
        //invio record
        producer.send(CsvRecord, (metadata, exception) -> {
            if(metadata != null){
                //successful writes
                System.out.println("msgSent: -> "+ myMessage.value());
            }
            else{
                //unsuccessful writes
                System.out.println("Error Sending Csv Record -> "+ myMessage.value());
            }
        });
        producer.close();
    }


    //metodo che crea proprietà per creare sink verso kafka
    public static Properties getFlinkPropAsProducer(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Config.KAFKA_BROKERS);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,Config.CLIENT_ID);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        return properties;

    }


    public static void main(String[] args) throws IOException {

        PublishMessages();

    }
}

