package com.example.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class SimpleProducer(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord)
        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
