package com.practice.kafka.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

abstract class Producer<T, U>(
    private val kafkaProducer: KafkaProducer<T, U>
) {
    private val logger = KotlinLogging.logger {}

    protected fun sendSync(topic: String, key: T, value: U) {
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

    protected fun sendAsync(topic: String, key: T, value: U) {
        val produceRecord = ProducerRecord(topic, key, value)
        logger.info { "Send key: $key, value: $value" }

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error(exception) { "Error sending record" }
            } else {
                logger.info { "Record sync sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}" }
            }
        }
    }
}
