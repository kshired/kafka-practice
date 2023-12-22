package com.example.kafka.config

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

class KafkaConfig {
    fun simpleProducer(): KafkaProducer<String, String> {
        val props = Properties().also {
            it[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
            it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        }
        return KafkaProducer(props)
    }

    companion object {
        private const val BOOTSTRAP_SERVERS = "192.168.56.101:9092"
    }
}
