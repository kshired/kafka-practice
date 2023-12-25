package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.producer.IntegerStringProducerAsync
import com.example.kafka.producer.PizzaProducer
import com.example.kafka.producer.SimpleProducer
import com.example.kafka.producer.SimpleProducerAsync
import com.example.kafka.producer.SimpleProducerSync
import kotlin.time.Duration.Companion.milliseconds

fun main() {
    val config = KafkaConfig()

    val simpleProducer = SimpleProducer(config.simpleProducer())
    simpleProducer.send("welcome-topic", "key", "value")

    val simpleProducerSync = SimpleProducerSync(config.simpleProducer())
    simpleProducerSync.send("welcome-topic", "key", "value")

    val simpleProducerAsync = SimpleProducerAsync(config.simpleProducer())
    simpleProducerAsync.send("welcome-topic", "key", "value")

    val integerStringProducerAsync = IntegerStringProducerAsync(config.integerStringProducer())
    integerStringProducerAsync.send("welcome-topic", 1, "value")

    val pizzaProducer = PizzaProducer(config.simpleProducer())
    pizzaProducer.sendPizzaMessage("pizza-topic", -1, 10.milliseconds, 100.milliseconds, 100, true)

    val customPartitionerPizzaProducer = PizzaProducer(config.customPartitionerProducer())
    customPartitionerPizzaProducer.sendPizzaMessage(
        "pizza-topic-partitioner",
        -1,
        10.milliseconds,
        100.milliseconds,
        100,
        true
    )
}
