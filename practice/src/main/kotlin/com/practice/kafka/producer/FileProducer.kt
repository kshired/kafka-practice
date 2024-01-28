package com.practice.kafka.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.io.FileReader

class FileProducer(
    private val kafkaProducer: KafkaProducer<String, String>
){
    private val logger = KotlinLogging.logger {}

    fun produce() {
        val resourceUrl = ClassLoader.getSystemClassLoader().getResource("pizza_sample.txt")
        val fileReader = FileReader(resourceUrl!!.path)
        val bufferedReader = fileReader.buffered()

        while (true) {
            val line = bufferedReader.readLine() ?: break
            val (key, value) = line.split(delimiters = arrayOf(","), limit = 2)
            send("file-topic", key, value)
        }
    }

    private fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)
        logger.info { "Send key: $key, value: $value" }

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error(exception) { "Error sending record" }
            } else {
                logger.info { "Record sync sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}" }
            }
        }
    }
}
