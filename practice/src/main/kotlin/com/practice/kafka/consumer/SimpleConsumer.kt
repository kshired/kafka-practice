package com.practice.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer

class SimpleConsumer(
    kafkaConsumer: KafkaConsumer<String, String>
) : BaseConsumer<String, String>(kafkaConsumer, false) {
    private val logger = KotlinLogging.logger {}

    override fun consume(topics: List<String>) {
        consume(topics) { records ->
            records.forEach {
                logger.info { "key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}, offset : ${it.offset()}" }
            }
        }
    }
}
