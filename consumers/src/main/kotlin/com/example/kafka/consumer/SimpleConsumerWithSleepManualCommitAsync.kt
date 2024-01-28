package com.example.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration

class SimpleConsumerWithSleepManualCommitAsync (
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

                if (records.count() > 0) {
                    kafkaConsumer.commitAsync { offsets, exception ->
                        if (exception != null) {
                            logger.error(exception) { "Error occurred while committing offsets : $offsets" }
                        } else {
                            logger.info { "Successfully committed offsets : $offsets" }
                        }
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
}
