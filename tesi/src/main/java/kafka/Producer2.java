package kafka;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.Config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static utils.Config.datasetPath;


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


    public static Timestamp createTimestamp(String date, String time) {
        //Timestamp format -> DD-MM-YYYY HH:MM:SS.ssss
        String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
        String dateTime = date + " " + time;
        //System.out.println("dateTime = " + dateTime);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date parsedDate = dateFormat.parse(dateTime);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }



    /*
    kafka producer streams messages to kafka topic reading csv file
     */
    //todo: mi serve lista di 5 valori (riportati nei field di elem)
    public static void PublishMessages() throws IOException {

        AtomicInteger count = new AtomicInteger(); //counts line (currently reading)
        List<String> csvTimestamps = new ArrayList<>();

        //todo: dichiara qua value + valueToSend e poi convertilo in atomic (dopo che ha funzionato con dichiarazione in forEach)
        final String[][] value = {new String[4]};
        final String[] valueToSend = new String[1];

        final Producer<String, String> producer = createProducer();
        long time = System.currentTimeMillis();

        Stream<String> FileStream = Files.lines(Paths.get(datasetPath+".csv"));

        //todo: calcola n righe da skippare cambiando il dataset. fai script bash per mettere un file da riga di comando in cartella "dataset"
        FileStream.skip(4).forEach(line -> {

            String[] lineFields = line.split(",");
            System.out.println("fields = " + lineFields[0]);

            //retrieving date and time of symbol's last received update to generate a timestamp
            Timestamp timestamp = createTimestamp(lineFields[2],lineFields[3]);
            System.out.println("timestamp = " + timestamp);
            System.out.println("timestamp STRING= " + timestamp.toString());
            Long ts = timestamp.getTime();    //ts to put into producerRecord
            System.out.println("ts = "+ts);

            //creating producer record (value) to send. it only contains data (from csv)actually useful for query's result
            value[0][0] = lineFields[1];        //sec type
            value[0][1]= timestamp.toString();  //ts for last received update
            value[0][2]= lineFields[21];        //last trade price
            value[0][3]= lineFields[23];        //last trade seconds
            valueToSend[0] = String.join(",", value[0]);


            //todo fai send e sistema accelerazione
            ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, ts, lineFields[0], valueToSend[0]);
            System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+producerRecord.value());

        });

        //invio dei messaggi
        ProducerRecord<String, String> myMessage = new ProducerRecord<>( Config.TOPIC1, "hello");

        //invio dei messaggi
        ProducerRecord<String, String> CsvRecord = new ProducerRecord<>( Config.TOPIC1, 0, "KEY", "VALLL");

        //invio record
        System.out.println("dopo MYMESSAGE!");
        
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


        //System.out.println("timestamp = " + createTimestamp("08-11-2021", "07:25:00.000"));
        PublishMessages();

    }
}

