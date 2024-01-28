package com.practice.kafka.consumer

import com.practice.kafka.config.KafkaConfig

fun main() {
    val simpleConsumer = SimpleConsumer(KafkaConfig().simpleConsumer())
    simpleConsumer.consume(listOf("file-topic"))
}
