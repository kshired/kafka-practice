package com.example.kafka.producer

import com.example.kafka.message.PizzaMessage
import com.github.javafaker.Faker
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Random
import kotlin.time.Duration

class PizzaProducer(
    private val kafkaProducer: KafkaProducer<String, String>
) : Producer<String, String> {
    private val logger = KotlinLogging.logger {}

    fun sendPizzaMessage(
        topic: String,
        iterCount: Int,
        interInterval: Duration,
        interval: Duration,
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
                logger.info { "IntervalCount : $intervalCount, intervalMillis : ${interval.inWholeMilliseconds}" }
                Thread.sleep(interval.inWholeMilliseconds)
            }

            if (interInterval > Duration.ZERO) {
                logger.info { "interIntervalMillis : ${interInterval.inWholeMilliseconds}" }
                Thread.sleep(interInterval.inWholeMilliseconds)
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
            logger.info { "Record async sent to partition ${recordMetadata.partition()} with offset ${recordMetadata.offset()} at timestamp ${recordMetadata.timestamp()}" }
        }.onFailure {
            logger.error(it) { "Error sending record" }
        }
    }


    override fun send(topic: String, key: String, value: String) {
        val produceRecord = ProducerRecord(topic, key, value)

        kafkaProducer.send(produceRecord) { metadata, exception ->
            if (exception != null) {
                logger.error(exception) { "Error sending record" }
            } else {
                logger.info { "Record sync sent to partition ${metadata?.partition()} with offset ${metadata?.offset()} at timestamp ${metadata?.timestamp()}" }
            }
        }
    }
}
