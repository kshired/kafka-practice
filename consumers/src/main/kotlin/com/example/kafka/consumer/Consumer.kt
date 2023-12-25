package com.example.kafka.consumer

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.LoggerFactory
import java.time.Duration

abstract class Consumer<T, U>(
    private val kafkaConsumer: KafkaConsumer<T, U>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun consume(topic: String, partition: Int = 0) {
        kafkaConsumer.assign(listOf(TopicPartition(topic, partition)))
        setUpShutDownHook()
        consume()
    }

    protected open fun consume() {
        runCatching {
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                records.forEach {
                    logger.info("key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}")
                }
            }
        }.onFailure {
            when (it) {
                is WakeupException -> logger.error("WakeupException occurred while consuming messages", it)
                else -> logger.error("Error occurred while consuming messages", it)
            }
        }.also {
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
