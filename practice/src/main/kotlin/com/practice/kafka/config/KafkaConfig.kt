package com.practice.kafka.config

import com.example.kafka.PropertiesBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

class KafkaConfig {
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
