package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.producer.SimpleProducer
import com.example.kafka.producer.SimpleProducerAsync
import com.example.kafka.producer.SimpleProducerSync

fun main() {
    val config = KafkaConfig()
    val props = config.props()

    val simpleProducer = SimpleProducer(props)
    simpleProducer.send("welcome-topic", "key", "value")

    val simpleProducerSync = SimpleProducerSync(props)
    simpleProducerSync.send("welcome-topic", "key", "value")

    val simpleProducerAsync = SimpleProducerAsync(props)
    simpleProducerAsync.send("welcome-topic", "key", "value")
}
