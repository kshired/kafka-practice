package com.practice.spring.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka.topics")
data class KafkaTopics(
    val orderTopic: String
)
