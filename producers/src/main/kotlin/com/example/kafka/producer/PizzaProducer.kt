package com.example.kafka.producer

import com.example.kafka.message.PizzaMessage
import com.github.javafaker.Faker
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.util.Random

class PizzaProducer(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendPizzaMessage(
        topic: String,
        iterCount: Int,
        interIntervalMillis: Int,
        intervalMillis: Int,
        intervalCount: Int,
        sync: Boolean
    ) {
        val pizzaMessage = PizzaMessage()
        var iterSeq = 0
        val seed = 2022L
        val random = Random(seed)
        val faker = Faker.instance(random)

        while (iterSeq != iterCount) {
            val message = pizzaMessage.produceMessage(faker, random, iterSeq)

            if (sync) {
                sendSync(topic, message["key"]!!, message["message"]!!)
            } else {
                send(topic, message["key"]!!, message["message"]!!)
            }

            if (intervalCount > 0 && iterSeq % intervalCount == 0) {
                logger.info("InteveralCount : $intervalCount, intervalMillis : $intervalMillis")
                Thread.sleep(intervalMillis.toLong())
            }

            if (interIntervalMillis > 0) {
                logger.info("interIntervalMillis : $intervalMillis")
                Thread.sleep(intervalMillis.toLong())
            }

            iterSeq++
        }

        kafkaProducer.flush()
        kafkaProducer.close()
    }

    private fun sendSync(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        runCatching {
            val recordMetadata = kafkaProducer.send(produceRecord).get()
            logger.info("Record async sent to partition ${recordMetadata.partition()} with offset ${recordMetadata.offset()} at timestamp ${recordMetadata.timestamp()}")
        }.onFailure {
            logger.error("Error sending record", it)
        }
    }


    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error("Error sending record")
            } else {
                logger.info("Record sync sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}")
            }
        }
    }
}
