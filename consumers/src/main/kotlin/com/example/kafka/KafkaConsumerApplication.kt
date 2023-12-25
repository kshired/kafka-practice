package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.consumer.SimpleConsumer

fun main() {
    val config = KafkaConfig()

    val simpleConsumer = SimpleConsumer(config.simpleConsumer())
    simpleConsumer.consume("simple-topic")

    val simpleConsumerForCheckingHeartBeat = SimpleConsumer(
        config.simpleConsumerForCheckingHeartBeat(
            heartBeatIntervalMs = "5000",
            sessionTimeoutMs = "90000",
            maxPollIntervalMs = "600000"
        )
    )
    simpleConsumerForCheckingHeartBeat.consume("simple-topic", shutDownGracefully = false)
}
