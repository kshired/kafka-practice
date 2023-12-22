package com.example.kafka.config

import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

class KafkaConfig {
    private var props: Properties? = null

    fun props(): Properties {
        return props ?: run {
            props = Properties().also {
                it[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "192.168.56.101:9092"
                it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
                it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            }
            props!!
        }
    }
}
