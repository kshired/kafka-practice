package com.practice.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration

abstract class BaseConsumer<T, U>(
    private val kafkaConsumer: KafkaConsumer<T, U>,
    private val commitSync: Boolean
) {
    private val logger = KotlinLogging.logger {}

    abstract fun consume(topics: List<String>)

    protected fun consume(
        topics: List<String>,
        callBack: (ConsumerRecord<T, U>) -> Unit
    ) {
        kafkaConsumer.subscribe(topics)
        setUpShutDownHook()
        consume(callBack)
    }

    private fun consume(callBack: (ConsumerRecord<T, U>) -> Unit) {
        runCatching {
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                records.forEach {
                    callBack(it)
                }
                if (records.count() > 0) {
                    if (commitSync) {
                        commitSync()
                    } else {
                        commitAsync()
                    }
                }
            }
        }.onFailure {
            when (it) {
                is WakeupException -> logger.error(it) { "WakeupException occurred while consuming messages" }
                else -> logger.error(it) { "Error occurred while consuming messages" }
            }
        }.also {
            kafkaConsumer.commitSync()
            logger.info { "Closing ${javaClass.simpleName}" }
            kafkaConsumer.close()
        }
    }

    private fun commitAsync() {
        kafkaConsumer.commitAsync { offsets, exception ->
            if (exception != null) {
                logger.error(exception) { "Error occurred while committing offsets : $offsets" }
            } else {
                logger.info { "Successfully committed offsets : $offsets" }
            }
        }
    }

    private fun commitSync() {
        runCatching {
            kafkaConsumer.commitSync()
            logger.info { "Successfully committed offsets" }
        }.onFailure {
            logger.error(it) { "Error occurred while committing offsets" }
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
