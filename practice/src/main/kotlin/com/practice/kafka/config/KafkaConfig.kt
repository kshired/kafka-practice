package com.practice.kafka.config

import com.example.kafka.PropertiesBuilder
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

class KafkaConfig {
    fun simpleConsumer(
        groupId: String = "group_01"
    ) : KafkaConsumer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keyDeserializerClass(StringDeserializer::class.java.name)
            .valueDeserializerClass(StringDeserializer::class.java.name)
            .groupIdConfig(groupId)
            .enableAutoCommit(false)
            .build()

        return KafkaConsumer(props)
    }

    fun simpleProducer(): KafkaProducer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keySerializerClass(StringSerializer::class.java.name)
            .valueSerializerClass(StringSerializer::class.java.name)
            .build()
        return KafkaProducer(props)
    }

    companion object {
        private const val BOOTSTRAP_SERVERS = "192.168.56.101:9092"
    }
}
