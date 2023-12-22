package com.example.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

class SimpleProducer(
    private val props: Properties
) : Producer<String, String> {
    override fun send(topic: String, key: String, value: String) {
        val kafkaProducer = KafkaProducer<String, String>(props)
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord)
        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
