package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.consumer.SimpleConsumer
import com.example.kafka.consumer.SimpleConsumerWithSleep
import com.example.kafka.consumer.SimpleConsumerWithSleepManualCommitAsync
import com.example.kafka.consumer.SimpleConsumerWithSleepManualCommitSync
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun main() {
    val config = KafkaConfig()

    val simpleConsumer = SimpleConsumer(config.simpleConsumer())
    simpleConsumer.consume(listOf("simple-topic"))

    val simpleConsumerForCheckingHeartBeat = SimpleConsumer(
        config.simpleConsumer(
            heartBeatInterval = 5.seconds,
            sessionTimeout = 90.seconds,
            maxPollInterval = 10.minutes
        )
    )
    simpleConsumerForCheckingHeartBeat.consume(listOf("simple-topic"), shutDownGracefully = false)

    val simpleConsumerWithSleep = SimpleConsumerWithSleep(
        config.simpleConsumer(
            groupId = "group_02",
            maxPollInterval = 2.seconds
        )
    )
    simpleConsumerWithSleep.consume(listOf("pizza-topic"))

    val simpleConsumerWithMultiTopic = SimpleConsumer(
        config.simpleConsumer(
            groupId = "group-assign",
            partitionAssignmentStrategy = CooperativeStickyAssignor::class.java.name
        )
    )
    simpleConsumerWithMultiTopic.consume(listOf("topic-p3-t1", "topic-p3-t2"))

    val simpleConsumerWithSleepManualCommitSync = SimpleConsumerWithSleepManualCommitSync(
        config.simpleConsumer(enableAutoCommit = false)
    )
    simpleConsumerWithSleepManualCommitSync.consume(listOf("pizza-topic"))

    val simpleConsumerWithSleepManualCommitAsync = SimpleConsumerWithSleepManualCommitAsync(
        config.simpleConsumer(enableAutoCommit = false)
    )
    simpleConsumerWithSleepManualCommitAsync.consume(listOf("pizza-topic"))

    val simpleConsumerSpecificPartition = SimpleConsumerWithSleepManualCommitAsync(
        config.simpleConsumer(
            groupId = "group_pizza_assig_seek",
            enableAutoCommit = false
        )
    )
    simpleConsumerSpecificPartition.consume(listOf("pizza-topic"), partition = 0)
}
