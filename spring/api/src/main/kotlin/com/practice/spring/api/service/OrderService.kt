package com.practice.spring.api.service

import com.practice.spring.kafka.config.KafkaTopics
import com.practice.spring.kafka.event.OrderEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class OrderService(
    @Qualifier("orderKafkaTemplate") private val orderKafkaTemplate: KafkaTemplate<String, OrderEvent>,
    private val kafkaTopics: KafkaTopics
) {
    private val logger = KotlinLogging.logger {}

    fun create(orderEvent: OrderEvent) {
        orderKafkaTemplate.send(kafkaTopics.orderTopic, orderEvent.orderId, orderEvent).whenComplete { result, exception ->
            if (exception != null) {
                throw exception
            } else {
                logger.info { "Order event sent: $result" }
            }
        }
    }
}
