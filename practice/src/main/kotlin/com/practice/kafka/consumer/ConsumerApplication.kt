package com.practice.kafka.consumer

import com.practice.kafka.config.KafkaConfig
import com.practice.kafka.order.OrderRepositoryImpl

fun main() {
    val simpleConsumer = SimpleConsumer(KafkaConfig().simpleConsumer())
    simpleConsumer.consume(listOf("file-topic"))

    val fileToDbConsumer = FileToDbConsumer(
        KafkaConfig().simpleConsumer(),
        OrderRepositoryImpl(
            username = "testuser",
            password = "test123!@#",
            url = "jdbc:postgresql://192.168.56.101:5432/postgres"
        )
    )
    fileToDbConsumer.consume(listOf("file-append-topic"))

    val fileToDbDeserializeConsumer = FileToDbDeserializeConsumer(
        KafkaConfig().orderDeserializerConsumer(),
        OrderRepositoryImpl(
            username = "testuser",
            password = "test123!@#",
            url = "jdbc:postgresql://192.168.56.101:5432/postgres"
        )
    )
    fileToDbDeserializeConsumer.consume(listOf("file-append-serialize-topic"))
}
