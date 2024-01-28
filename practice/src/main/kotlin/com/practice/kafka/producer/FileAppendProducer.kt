package com.practice.kafka.producer

import com.practice.kafka.event.FileEventHandler
import com.practice.kafka.event.FileEventSource
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import java.io.File
import kotlin.time.Duration.Companion.seconds

class FileAppendProducer(
    private val kafkaProducer: KafkaProducer<String, String>
) {
    private val logger = KotlinLogging.logger {}

    fun produce() {
        val resourceUrl = ClassLoader.getSystemClassLoader().getResource("pizza_append.txt")
        val file = File(resourceUrl!!.path)

        val fileEventHandler = FileEventHandler(kafkaProducer, "file-append-topic", false)
        val fileEventSource = FileEventSource(fileEventHandler, 1.seconds, file)
        val fileEventSourceThread = Thread(fileEventSource)

        fileEventSourceThread.start()
        runCatching {
            fileEventSourceThread.join()
        }.onFailure {
            logger.error(it) { it.message }
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
