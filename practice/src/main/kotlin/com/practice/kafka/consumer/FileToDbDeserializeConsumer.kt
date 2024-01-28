package com.practice.kafka.consumer

import com.practice.kafka.order.Order
import com.practice.kafka.order.OrderRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer

class FileToDbDeserializeConsumer(
    kafkaConsumer: KafkaConsumer<String, Order>,
    private val orderRepository: OrderRepository
) : BaseConsumer<String, Order>(kafkaConsumer, false) {
    private val logger = KotlinLogging.logger {}

    override fun consume(topics: List<String>) {
        consume(topics) { records ->
            orderRepository.saveAll(records.map { it.value() })
            records.forEach {
                logger.info { "Save key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}, offset : ${it.offset()}" }
            }
        }
        orderRepository.close()
    }
}
