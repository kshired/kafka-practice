package com.example.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

class SimpleProducerAsync(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error("Error sending record", exception)
            } else {
                logger.info("Record sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}")
            }
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
