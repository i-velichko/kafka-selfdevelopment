package com.ivanvelichko.selfdevelopment;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoKeys {
    public static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer!");

        Properties properties = new Properties();
        //connect to Localhost
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        //connect to secure cluster on Conductor
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"6b4efIQJCjWTosu8e84u25\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI2YjRlZklRSkNqV1Rvc3U4ZTg0dTI1Iiwib3JnYW5pemF0aW9uSWQiOjczMDUwLCJ1c2VySWQiOjg0OTIxLCJmb3JFeHBpcmF0aW9uQ2hlY2siOiJiYjNhYzZhNi1kZDc2LTQxMDctYTlkYy01YzNlYzRiMmYwZWQifX0.OG9Xq40CedBik8sA7GEH5t10mALCsQfztiZkXD9iMu8\";");
        properties.setProperty("sasl.mechanism", "PLAIN");

        //set producer props
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 2; j++) {

            for (int i = 0; i < 10; i++) {

                String topic = "demo_java";
                String key = "id_" + i;
                String value = "hello kafka " + i;

                //create ProdRec and send data
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<String, String>(topic, key, value);

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        //executes every time a record successfully send or an exception is thrown
                        if (exception == null) {
                            //the record was successfully send

                            log.info("Key: " + key + " | Partition: " + metadata.partition());
                        } else {
                            log.error("Error while producing", exception);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        //flush and close producer

        //tell the producer to send all data and block until done - synchronous
        producer.flush();

        producer.close();

    }
}
