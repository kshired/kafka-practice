package com.example.kafka.config

import com.example.kafka.PropertiesBuilder
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

class KafkaConfig {
    fun simpleConsumer(groupId: String = "group_01", staticInstanceId: String? = null) : KafkaConsumer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keyDeserializerClass(StringDeserializer::class.java.name)
            .valueDeserializerClass(StringDeserializer::class.java.name)
            .groupIdConfig(groupId)
            .apply {
                staticInstanceId?.let {
                    groupInstanceIdConfig(it)
                }
            }.build()

        return KafkaConsumer(props)
    }

    fun simpleConsumerForCheckingHeartBeat(
        groupId: String = "group_01",
        heartBeatIntervalMs: String,
        sessionTimeoutMs: String,
        maxPollIntervalMs: String
    ) : KafkaConsumer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keyDeserializerClass(StringDeserializer::class.java.name)
            .valueDeserializerClass(StringDeserializer::class.java.name)
            .groupIdConfig(groupId)
            .heartBeatIntervalMsConfig(heartBeatIntervalMs)
            .sessionTimeoutMsConfig(sessionTimeoutMs)
            .maxPollIntervalMsConfig(maxPollIntervalMs)
            .build()

        return KafkaConsumer(props)
    }

    companion object {
        private const val BOOTSTRAP_SERVERS = "192.168.56.101:9092"
    }
}
