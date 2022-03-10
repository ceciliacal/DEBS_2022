package kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.Config;

import static utils.Config.*;


public class MyProducer {

    private static final SimpleDateFormat[] dateFormats = {new SimpleDateFormat("dd/MM/yy HH:mm"), new SimpleDateFormat("dd-MM-yy HH:mm")};
    private static final SimpleDateFormat time = timeFormat;
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
        System.out.println("PROVA");



        try {
            //stream verso file csv
            String filename="debs-2022-gc-test-set-trading.csv";
            Path pathToFile = Paths.get(filename);
            System.out.println("--PATH: "+pathToFile.toAbsolutePath());

            Stream<String> FileStream = Files.lines(Paths.get(datasetPath+".csv"));


            //rimozione dell'header e lettura del file
            FileStream.skip(2).forEach(line -> {

                Long sleepTime = null;

                System.out.println("------------------------START----------------------");
                count.getAndIncrement();

                String[] fields = line.split(",");
                String[] value = Arrays.copyOfRange(fields, 1, fields.length);
                String tsCurrent = value[3];    //timestamp current line

                System.out.println("tsCurrent = " + tsCurrent);
                Long date = null;

                try {
                    date = time.parse(tsCurrent).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (SimpleDateFormat dateFormat: dateFormats) {
                    try {
                        date = dateFormat.parse(tsCurrent).getTime();
                        break;
                    } catch (ParseException ignored) { }
                }

                System.out.println("line: " + line);


                //ritardo l'invio delle tuple al broker in base
                // alla differenza tra i timestamp di due msg consecutivi
                if (count.get() > 1) {
                    try {
                        sleepTime = simulateStream(tsCurrent, csvTimestamps, count.get());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                //aggiungo ts alla lista (a partire dal primo elemento)
                csvTimestamps.add(tsCurrent);

                if (sleepTime!=null){
                    try {
                        System.out.println("sleep time: "+sleepTime+" millisec");
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String dateStrKey = String.valueOf(date);

                //invio dei messaggi
                ProducerRecord<String, String> CsvRecord = new ProducerRecord<>( Config.TOPIC1, 0, date, dateStrKey, line);

                //invio record
                producer.send(CsvRecord, (metadata, exception) -> {
                    if(metadata != null){
                        //successful writes
                        System.out.println("CsvData: -> "+ CsvRecord.key()+" | "+ CsvRecord.value());
                    }
                    else{
                        //unsuccessful writes
                        System.out.println("Error Sending Csv Record -> "+ CsvRecord.value());
                    }
                });

                String tsLast = value[6];
                System.out.println("------------------------END----------------------");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("lista csvTimeStamps: "+ csvTimestamps.size());


    }


             //metodo che calcola tempo di ritardo dell'invio dei messaggi
    private static long simulateStream(String currentTs, List<String> tsList, int i) throws ParseException {

        System.out.println("-------------STO IN SIMULATE=============");

        String lastTs = tsList.get(i-2);
        Long lastTsTime = null;
        Long currentTsTime = null;

        for (SimpleDateFormat dateFormat: dateFormats) {
            try {
                //prendo ts del messaggio precedente rispetto a quello che sto leggendo attualmente
                lastTsTime = dateFormat.parse(lastTs).getTime();
                break;
            } catch (ParseException ignored) { }
        }

        for (SimpleDateFormat dateFormat: dateFormats) {
            try {
                currentTsTime = dateFormat.parse(currentTs).getTime();
                break;
            } catch (ParseException ignored) { }
        }

        //calcolo differenza in millisecondi fra timestamp corrente e quello precedente
        //in modo da simulare il ritardo in base al tempo che intercorre fra i due ts
        double diff = currentTsTime - lastTsTime;
        Long sleepTimeMillis =  (long) (diff*Config.accTime);

        //per una differenza di 2 min nei ts, dormo 2 ms
        //quindi converto differenza di minuti in millisecondi
        Long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(sleepTimeMillis);

        System.out.println("--- minutesDifference: "+ minutesDifference);

        return minutesDifference;

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

