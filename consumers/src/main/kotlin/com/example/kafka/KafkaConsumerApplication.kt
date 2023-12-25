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
            heartBeatInterval = 5.seconds,
            sessionTimeout = 90.seconds,
            maxPollInterval = 10.minutes
        )
    )
    simpleConsumerForCheckingHeartBeat.consume("simple-topic", shutDownGracefully = false)

    val simpleConsumerWithSleep = SimpleConsumerWithSleep(
        config.simpleConsumer(
            groupId = "group_02",
            maxPollInterval = 2.seconds
        )
    )
    simpleConsumerWithSleep.consume("pizza-topic")
}
