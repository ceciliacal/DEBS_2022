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
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static utils.Config.accTime;
import static utils.Config.datasetPath;

//TODO: rivedi questione timestamp sia su kafdrop e controlla sul consumer come arriva!

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
    Creates Timestamp object from symbol's last received update
     */
    public static Timestamp createTimestamp(String date, String time) {
        //Timestamp format -> DD-MM-YYYY HH:MM:SS.ssss
        String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
        String dateTime = date + " " + time;
        //System.out.println("dateTime = " + dateTime);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date parsedDate = dateFormat.parse(dateTime);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) {
            //error
            return null;
        }

    }



    /*
    kafka producer streams messages to kafka topic reading csv file
     */
    public static void PublishMessages() throws IOException {

        AtomicLong prevTs = new AtomicLong();       //previous line timestamp
        AtomicLong currTs = new AtomicLong();       //current line timestamp
        AtomicLong tsDifference = new AtomicLong(); //difference between current and previous ts
        AtomicInteger previous = new AtomicInteger();
        previous.set(0);

        final String[][] value = {new String[5]};
        final String[] valueToSend = new String[1];
        final Producer<String, String> producer = createProducer();

        Stream<String> FileStream = Files.lines(Paths.get(datasetPath+".csv"));

        //todo: calcola n righe da skippare cambiando  il dataset. fai script bash per mettere un file da riga di comando in cartella "dataset"
        FileStream.skip(4).forEach(line -> {

            String[] lineFields = line.split(",");

            //retrieving date and time of symbol's last received update to generate a timestamp
            Timestamp timestamp = createTimestamp(lineFields[2],lineFields[3]);
            System.out.println("timestamp = " + timestamp);
            //todo assert ts != null
            currTs.set(timestamp.getTime());    //ts to put into producerRecord
            System.out.println("ts = "+currTs);

            //creating producer record (value) to send. it only contains data (from csv)actually useful for query's result
            value[0][0] = lineFields[0];        //sec type
            value[0][1] = lineFields[1];        //sec type
            value[0][2]= timestamp.toString();  //ts for last received update
            value[0][3]= lineFields[21];        //last trade price
            value[0][4]= lineFields[23];        //last trade seconds
            valueToSend[0] = String.join(",", value[0]);


            //sistema accelerazione
            //cioè se sto dalla seconda riga in poi mi calcolo la differenza tra i long e poi faccio sleep di quei ms
            if (previous.get()!=0){

                tsDifference.set(currTs.get() - prevTs.get());
                System.out.println("---tsDifference = "+tsDifference);
                Long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(tsDifference.get());
                System.out.println("--- minutesDifference: "+ minutesDifference);

                //sleep time
                System.out.println("sleep time: "+minutesDifference*accTime+" millisec");
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (minutesDifference*accTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, currTs.get(), lineFields[0], valueToSend[0]);
            System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+producerRecord.value());

            //todo fai send
            producer.send(producerRecord, (metadata, exception) -> {
                if(metadata != null){
                    //successful writes
                    System.out.println("msgSent: ->  key: "+producerRecord.key()+" value: "+ producerRecord.value());
                }
                else{
                    //unsuccessful writes
                    System.out.println("Error Sending Csv Record -> key: " + producerRecord.key()+" value: " + producerRecord.value());
                }
            });

            prevTs.set(currTs.get());     //current ts is set to previous ts for next iteration
            previous.set(1);

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
        //properties.put(ProducerConfig.LINGER_MS_CONFIG);

        return properties;

    }


    public static void main(String[] args) throws IOException {

        //System.out.println("timestamp = " + createTimestamp("08-11-2021", "07:25:00.000"));
        PublishMessages();

    }
}

