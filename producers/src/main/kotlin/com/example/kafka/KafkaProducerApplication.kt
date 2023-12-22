package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.producer.IntegerStringProducerAsync
import com.example.kafka.producer.SimpleProducer
import com.example.kafka.producer.SimpleProducerAsync
import com.example.kafka.producer.SimpleProducerSync

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
}
