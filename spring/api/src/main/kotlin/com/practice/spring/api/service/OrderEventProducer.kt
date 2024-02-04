package com.practice.spring.api.service

import com.practice.spring.kafka.event.OrderEvent

interface OrderEventProducer {
    fun produce(orderEvent: OrderEvent)
}
