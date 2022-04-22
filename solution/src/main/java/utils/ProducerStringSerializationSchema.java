package utils;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

public class ProducerStringSerializationSchema implements KafkaSerializationSchema<String> {

    private String topic;

    public ProducerStringSerializationSchema(String topic) {
        super();
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, Long timestamp) {
        System.out.println("element len = "+ element.length());
        return new ProducerRecord<>(topic, element.getBytes(StandardCharsets.UTF_8));
    }


}
