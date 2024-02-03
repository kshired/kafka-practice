package com.practice.spring.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
data class KafkaConfigProperties(
    val bootstrapServers: String
)
