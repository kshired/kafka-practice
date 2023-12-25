package com.example.kafka.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class IntegerStringProducerAsync(
    private val kafkaProducer: KafkaProducer<Int, String>
) : Producer<Int, String> {
    private val logger = KotlinLogging.logger {}

    override fun send(topic: String, key: Int, value: String) {
        (1..20).forEach {
            val producerRecord = ProducerRecord(topic, it, "$value $it")
            logger.info { "sequence : $it" }

            kafkaProducer.send(producerRecord) { metadata, exception ->
                if (exception != null) {
                    logger.error(exception) { "Error sending record" }
                } else {
                    logger.info { "Record sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}" }
                }
            }
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
