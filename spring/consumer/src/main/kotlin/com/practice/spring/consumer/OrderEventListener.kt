package com.practice.spring.consumer

import com.practice.spring.kafka.event.OrderEvent
import com.practice.spring.repository.OrderEntity
import com.practice.spring.repository.OrderRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(
    private val orderRepository: OrderRepository
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["\${kafka.topics.order-topic}"],
        groupId = "\${kafka.consumer.group-id}",
        containerFactory = "orderEventContainerFactory"
    )
    fun handleOrderEvent(orderEvent: OrderEvent) {
        require (orderRepository.findByIdOrNull(orderEvent.orderId) == null) {
            "Order already exists"
        }
        orderRepository.save(orderEvent.toOrderEntity())
        logger.info { "order event saved :$orderEvent" }
    }

    private fun OrderEvent.toOrderEntity() = OrderEntity(
        orderId = orderId,
        shopId = shopId,
        menuName = menuName,
        userName = userName,
        phoneNumber = phoneNumber,
        address = address,
        orderTime = orderTime
    )
}
