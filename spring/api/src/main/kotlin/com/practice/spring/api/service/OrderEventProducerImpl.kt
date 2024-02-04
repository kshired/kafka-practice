package com.practice.spring.api.service

import com.practice.spring.kafka.config.KafkaTopics
import com.practice.spring.kafka.event.OrderEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventProducerImpl(
    private val orderKafkaTemplate: KafkaTemplate<String, OrderEvent>,
    private val kafkaTopics: KafkaTopics
) : OrderEventProducer {
    private val logger = KotlinLogging.logger {}

    override fun produce(orderEvent: OrderEvent) {
        orderKafkaTemplate.send(kafkaTopics.orderTopic, orderEvent.orderId, orderEvent).whenComplete { _, exception ->
            if (exception != null) {
                logger.error { "Failed to produce order event: $orderEvent" }
            } else {
                logger.info { "Order event produced: $orderEvent" }
            }
        }
    }
}
