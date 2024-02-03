package com.practice.spring.kafka.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.common.serialization.Serializer

class CustomJsonSerializer : Serializer<Any> {
    private val logger = KotlinLogging.logger {}
    private val objectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    override fun serialize(topic: String?, data: Any?): ByteArray {
        return runCatching {
            objectMapper.writeValueAsBytes(data)
        }.onFailure {
            logger.error(it) { "Failed to serialize data: $data" }
        }.getOrThrow()
    }
}
