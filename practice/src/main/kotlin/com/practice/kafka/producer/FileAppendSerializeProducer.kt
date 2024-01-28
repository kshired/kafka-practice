package com.practice.kafka.producer

import com.practice.kafka.event.FileEventSerializeHandler
import com.practice.kafka.event.FileEventSource
import com.practice.kafka.order.Order
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import java.io.File
import kotlin.time.Duration.Companion.seconds

class FileAppendSerializeProducer(
    private val kafkaProducer: KafkaProducer<String, Order>
)  {
    private val logger = KotlinLogging.logger {}

    fun produce() {
        val resourceUrl = ClassLoader.getSystemClassLoader().getResource("pizza_append.txt")
        val file = File(resourceUrl!!.path)

        val fileEventHandler = FileEventSerializeHandler(kafkaProducer, "file-append-serialize-topic", false)
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
