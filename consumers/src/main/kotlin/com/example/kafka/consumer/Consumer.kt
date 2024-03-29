package com.example.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration

abstract class Consumer<T, U>(
    private val kafkaConsumer: KafkaConsumer<T, U>
) {
    private val logger = KotlinLogging.logger {}

    fun consume(
        topics: List<String>,
        shutDownGracefully: Boolean = true,
        partition: Int? = null,
        offset: Long? = null
    ) {
        partition?.let {
            if (topics.size != 1) {
                throw IllegalArgumentException("partition is not null, but topics size is not 1")
            }
            kafkaConsumer.assign(listOf(TopicPartition(topics[0], partition)))
            if (offset != null) {
                kafkaConsumer.seek(TopicPartition(topics[0], partition), offset)
            }
        } ?: run {
            kafkaConsumer.subscribe(topics)
        }
        if (shutDownGracefully) {
            setUpShutDownHook()
        }
        consume()
    }

    protected open fun consume() {
        runCatching {
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                records.forEach {
                    logger.info { "topic : ${it.topic()}, key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}, offset : ${it.offset()}" }
                }
            }
        }.onFailure {
            when (it) {
                is WakeupException -> logger.error(it) { "WakeupException occurred while consuming messages" }
                else -> logger.error(it) { "Error occurred while consuming messages" }
            }
        }.also {
            logger.info { "Closing ${javaClass.simpleName}" }
            kafkaConsumer.close()
        }
    }

    private fun setUpShutDownHook() {
        val mainThread = Thread.currentThread()
        Runtime.getRuntime().addShutdownHook(Thread {
            kafkaConsumer.wakeup()
            mainThread.join()
        })
    }
}
