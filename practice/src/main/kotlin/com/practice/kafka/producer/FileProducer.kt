package com.practice.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import java.io.FileReader

class FileProducer(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String>(kafkaProducer) {
    fun produce() {
        val resourceUrl = ClassLoader.getSystemClassLoader().getResource("pizza_sample.txt")
        val fileReader = FileReader(resourceUrl!!.path)
        val bufferedReader = fileReader.buffered()

        while (true) {
            val line = bufferedReader.readLine() ?: break
            val (key, value) = line.split(delimiters = arrayOf(","), limit = 2)
            sendAsync("file-topic", key, value)
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}
