package com.example.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration

class SimpleConsumerWithSleepManualCommitSync (
    private val kafkaConsumer: KafkaConsumer<String, String>
) : Consumer<String, String>(kafkaConsumer) {
    private val logger = KotlinLogging.logger {}

    override fun consume() {
        runCatching {
            var loopCnt = 0
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                logger.info { "loopCnt : ${loopCnt++}, records count : ${records.count()}" }
                records.forEach {
                    logger.info { "key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}, offset : ${it.offset()}" }
                }
                logger.info { "main thread is sleeping ${loopCnt*100} ms" }
                Thread.sleep(loopCnt*100L)

                runCatching {
                    if (records.count() > 0) {
                        kafkaConsumer.commitSync()
                        logger.info { "Successfully committed offsets" }
                    }
                }.onFailure {
                    logger.error(it) { "Error occurred while committing offsets" }
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
}
