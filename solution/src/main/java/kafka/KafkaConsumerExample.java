package kafka;
import de.tum.i13.challenge.Benchmark;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private final static String TOPIC = "resultsTopic";
    private final static String BOOTSTRAP_SERVERS = "localhost:9091";
    Benchmark newBenchmark;

    public KafkaConsumerExample(Benchmark newBenchmark) {
        this.newBenchmark = newBenchmark;
    }

    public Benchmark getNewBenchmark() {
        return newBenchmark;
    }

    public void setNewBenchmark(Benchmark newBenchmark) {
        this.newBenchmark = newBenchmark;
    }


    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public void runConsumer() throws InterruptedException {

        final Consumer<Long, String> consumer = createConsumer();

        final int giveUp = 100;
        int noRecordsCount = 0;

        System.out.println("in runconsumer!!!!!!!!!!!!!");
        System.out.println("in runcons: newBenchmark = "+ newBenchmark);

        while (true) {
            //System.out.println("nel while");
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);

            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            //System.out.println("prima di foreach record");
            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();
        }


        consumer.close();
        System.out.println("DONE");
    }
}
