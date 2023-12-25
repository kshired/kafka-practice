package com.example.kafka.config

import com.example.kafka.PropertiesBuilder
import com.example.kafka.partitioner.CustomPartitioner
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.IntegerSerializer
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

    fun integerStringProducer(): KafkaProducer<Int, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keySerializerClass(IntegerSerializer::class.java.name)
            .valueSerializerClass(StringSerializer::class.java.name)
            .build()
        return KafkaProducer(props)
    }

    fun customPartitionerProducer(): KafkaProducer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keySerializerClass(StringSerializer::class.java.name)
            .valueSerializerClass(StringSerializer::class.java.name)
            .partitionerClassConfig(CustomPartitioner::class.java.name)
            .addCustomConfig("custom.specialKey", "P001")
            .build()

        return KafkaProducer(props)
    }

    companion object {
        private const val BOOTSTRAP_SERVERS = "192.168.56.101:9092"
    }
}
