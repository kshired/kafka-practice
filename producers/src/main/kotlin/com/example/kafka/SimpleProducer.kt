package com.example.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

fun main() {
    val props = Properties()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "192.168.56.101:9092"
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

    val kafkaProducer = KafkaProducer<String, String>(props)
    val topic = "welcome-topic"
    val produceRecord = ProducerRecord<String, String>(topic, "Hello", "Kafka!")

    kafkaProducer.send(produceRecord)
    kafkaProducer.flush()
    kafkaProducer.close()
}
