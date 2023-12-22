package com.example.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

class SimpleProducerSync(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        runCatching {
            val recordMetadata = kafkaProducer.send(produceRecord).get()
            logger.info("Record sent to partition ${recordMetadata.partition()} with offset ${recordMetadata.offset()} at timestamp ${recordMetadata.timestamp()}")
        }.onFailure {
            logger.error("Error sending record", it)
        }

        kafkaProducer.flush()
        kafkaProducer.close()
        kafkaProducer.flush()
        kafkaProducer.close()
    }
}