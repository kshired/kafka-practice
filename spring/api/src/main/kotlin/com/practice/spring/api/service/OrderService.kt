package com.practice.spring.api.service

import com.practice.spring.kafka.event.OrderEvent
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderEventProducer: OrderEventProducer
) {
    fun create(orderEvent: OrderEvent) {
        orderEventProducer.produce(orderEvent)
    }
}
