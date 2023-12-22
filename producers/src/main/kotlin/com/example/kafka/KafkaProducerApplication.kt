package com.example.kafka

import com.example.kafka.config.KafkaConfig
import com.example.kafka.producer.SimpleProducer

fun main() {
    val config = KafkaConfig()
    val props = config.props()

    val simpleProducer = SimpleProducer(props)
    simpleProducer.send("welcome-topic", "key", "value")
}
