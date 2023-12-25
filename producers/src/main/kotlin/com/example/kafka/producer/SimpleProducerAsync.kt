package com.example.kafka.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class SimpleProducerAsync(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = KotlinLogging.logger {}

    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error(exception) { "Error sending record" }
            } else {
                logger.info { "Record sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}" }
            }
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
