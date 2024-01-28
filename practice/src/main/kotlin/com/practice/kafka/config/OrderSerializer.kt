package com.practice.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.practice.kafka.order.Order
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.common.serialization.Serializer

class OrderSerializer : Serializer<Order> {
    private val logger = KotlinLogging.logger {}
    private val objectMapper = ObjectMapper().registerModules(kotlinModule(), JavaTimeModule())

    override fun serialize(topic: String, data: Order): ByteArray? {
        return runCatching {
            objectMapper.writeValueAsBytes(data)
        }.onFailure {
            logger.error(it) { it.message }
        }.getOrNull()
    }
}
