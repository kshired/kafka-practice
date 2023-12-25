package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.consumer.SimpleConsumer
import com.example.kafka.consumer.SimpleConsumerWithSleep
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun main() {
    val config = KafkaConfig()

    val simpleConsumer = SimpleConsumer(config.simpleConsumer())
    simpleConsumer.consume("simple-topic")

    val simpleConsumerForCheckingHeartBeat = SimpleConsumer(
        config.simpleConsumer(
            heartBeatIntervalMs = 5.seconds,
            sessionTimeoutMs = 90.seconds,
            maxPollIntervalMs = 10.minutes
        )
    )
    simpleConsumerForCheckingHeartBeat.consume("simple-topic", shutDownGracefully = false)

    val simpleConsumerWithSleep = SimpleConsumerWithSleep(
        config.simpleConsumer(
            groupId = "group_02",
            maxPollIntervalMs = 2.seconds
        )
    )
    simpleConsumerWithSleep.consume("pizza-topic")
}
