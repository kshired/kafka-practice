package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.consumer.SimpleConsumer

fun main() {
    val config = KafkaConfig()

    val simpleConsumer = SimpleConsumer(config.simpleConsumer())
    simpleConsumer.consume("simple-topic")
}
