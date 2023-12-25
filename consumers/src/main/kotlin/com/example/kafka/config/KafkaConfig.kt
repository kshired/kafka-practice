package com.example.kafka.config

import com.example.kafka.PropertiesBuilder
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import kotlin.time.Duration

class KafkaConfig {
    fun simpleConsumer(
        groupId: String = "group_01",
        staticInstanceId: String? = null,
        heartBeatIntervalMs: Duration? = null,
        sessionTimeoutMs: Duration? = null,
        maxPollIntervalMs: Duration? = null
    ) : KafkaConsumer<String, String> {
        val props = PropertiesBuilder()
            .bootStrapServer(BOOTSTRAP_SERVERS)
            .keyDeserializerClass(StringDeserializer::class.java.name)
            .valueDeserializerClass(StringDeserializer::class.java.name)
            .groupIdConfig(groupId)
            .apply {
                staticInstanceId?.let {
                    groupInstanceIdConfig(it)
                }
                heartBeatIntervalMs?.let {
                    this.heartBeatIntervalMsConfig(it.inWholeMilliseconds.toString())
                }
                sessionTimeoutMs?.let {
                    this.sessionTimeoutMsConfig(it.inWholeMilliseconds.toString())
                }
                maxPollIntervalMs?.let {
                    this.maxPollIntervalMsConfig(it.inWholeMilliseconds.toString())
                }
            }.build()

        return KafkaConsumer(props)
    }

    companion object {
        private const val BOOTSTRAP_SERVERS = "192.168.56.101:9092"
    }
}
