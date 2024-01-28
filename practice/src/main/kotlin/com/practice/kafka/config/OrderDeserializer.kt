package com.practice.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.practice.kafka.order.Order
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.common.serialization.Deserializer

class OrderDeserializer : Deserializer<Order> {
    private val logger = KotlinLogging.logger {}
    private val objectMapper = ObjectMapper().registerModules(kotlinModule(), JavaTimeModule())

    override fun deserialize(topic: String, data: ByteArray): Order? {
        return runCatching {
            objectMapper.readValue(data, Order::class.java)
        }.onFailure {
            logger.error(it) { it.message }
        }.getOrNull()
    }
}
