package com.example.kafka.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class SimpleProducerSync(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = KotlinLogging.logger {}
    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        runCatching {
            val recordMetadata = kafkaProducer.send(produceRecord).get()
            logger.info { "Record sent to partition ${recordMetadata.partition()} with offset ${recordMetadata.offset()} at timestamp ${recordMetadata.timestamp()}" }
        }.onFailure {
            logger.error(it) { "Error sending record" }
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
