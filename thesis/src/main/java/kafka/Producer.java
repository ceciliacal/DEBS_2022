package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.Config;

import javax.sound.midi.spi.SoundbankReader;
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

import static data.Event.createTimestamp;
import static utils.Config.accTime;
import static utils.Config.datasetPath;

//TODO: rivedi questione timestamp sia su kafdrop e controlla sul consumer come arriva!

public class Producer {

    /*
    creates kafka producer
     */
    public static org.apache.kafka.clients.producer.Producer<String, String> createProducer() {
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

        AtomicLong prevTs = new AtomicLong();       //previous line timestamp
        AtomicLong currTs = new AtomicLong();       //current line timestamp
        AtomicLong tsDifference = new AtomicLong(); //difference between current and previous ts
        AtomicInteger previous = new AtomicInteger();
        previous.set(0);

        final String[][] value = {new String[4]};
        final String[] valueToSend = new String[1];
        final org.apache.kafka.clients.producer.Producer<String, String> producer = createProducer();

        Stream<String> FileStream = Files.lines(Paths.get("ciao"+".csv"));

        //todo: calcola n righe da skippare cambiando  il dataset. fai script bash per mettere un file da riga di comando in cartella "dataset"
        FileStream.skip(0).forEach(line -> {

            String[] lineFields = line.split(",");

            //retrieving Date and Time of symbol's last received update to generate a timestamp

            System.out.println("lineFields = "+lineFields);
            System.out.println("lineFields size= "+lineFields.length);
            System.out.println("lineFields[1] = "+lineFields[1]);
            System.out.println("lineFields[2] = "+lineFields[2]);


            Timestamp lastUpdateTs = createTimestamp(lineFields[2],lineFields[3]);
            if (lastUpdateTs == null){
                //error
                System.out.println("error in producer: ts is null");
                return;
            }

            currTs.set(lastUpdateTs.getTime());    //ts to put into producerRecord
            System.out.println("timestamp UPDATE= " + lastUpdateTs);
            System.out.println("ts = "+currTs);


            //creating producer record (value) to send. it only contains data (from csv)actually useful for query's result
            value[0][0] = lineFields[0];                //sec type
            value[0][1] = lineFields[1];                //sec type
            value[0][2]= lastUpdateTs.toString();       //ts for last received update
            value[0][3]= lineFields[21];                //last trade price

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

            if (value[0][2].contains("08:00:00")){
                System.out.println("STO PER FARE SLEEP 20 SEC!!!!");
                try {
                    TimeUnit.SECONDS.sleep((long) 20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ProducerRecord<String,String> producerRecord= new ProducerRecord<>(Config.TOPIC1, 0, currTs.get(), lineFields[0], valueToSend[0]);
            System.out.println("producerRecord-> long: "+ producerRecord.timestamp()+ " key: "+producerRecord.key()+" value: "+producerRecord.value());

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

