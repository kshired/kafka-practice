package com.example.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import java.util.Properties

class PropertiesBuilder {
    private var bootStrapServer: String? = null
    private var keySerializerClass: String? = null
    private var keyDeserializerClass: String? = null
    private var valueSerializerClass: String? = null
    private var valueDeserializerClass: String? = null
    private var groupIdConfig: String? = null
    private var partitionerClassConfig: String? = null
    private var groupInstanceIdConfig: String? = null
    private var customConfig: Map<String, String> = emptyMap()

    fun bootStrapServer(bootStrapServer: String) = apply {
        this.bootStrapServer = bootStrapServer
    }

    fun keySerializerClass(keySerializerClass: String) = apply {
        this.keySerializerClass = keySerializerClass
    }

    fun keyDeserializerClass(keyDeserializerClass: String) = apply {
        this.keyDeserializerClass = keyDeserializerClass
    }

    fun valueSerializerClass(valueSerializerClass: String) = apply {
        this.valueSerializerClass = valueSerializerClass
    }

    fun valueDeserializerClass(valueDeserializerClass: String) = apply {
        this.valueDeserializerClass = valueDeserializerClass
    }

    fun groupIdConfig(groupIdConfig: String) = apply {
        this.groupIdConfig = groupIdConfig
    }

    fun partitionerClassConfig(partitionerClassConfig: String) = apply {
        this.partitionerClassConfig = partitionerClassConfig
    }

    fun groupInstanceIdConfig(groupInstanceIdConfig: String) = apply {
        this.groupInstanceIdConfig = groupInstanceIdConfig
    }

    fun addCustomConfig(key: String, value: String) = apply {
        this.customConfig = customConfig.plus(Pair(key, value))
    }

    fun build() : Properties {
        return Properties().also {
            bootStrapServer?.let { bootStrapServer ->
                it[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootStrapServer
                it[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootStrapServer
            }
            keySerializerClass?.let { keySerializerClass ->
                it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializerClass
            }
            valueSerializerClass?.let { valueSerializerClass ->
                it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializerClass
            }
            partitionerClassConfig?.let { partitionerClassConfig ->
                it[ProducerConfig.PARTITIONER_CLASS_CONFIG] = partitionerClassConfig
            }
            keyDeserializerClass?.let { keyDeserializerClass ->
                it[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializerClass
            }
            valueDeserializerClass?.let { valueDeserializerClass ->
                it[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializerClass
            }
            groupIdConfig?.let { groupIdConfig ->
                it[ConsumerConfig.GROUP_ID_CONFIG] = groupIdConfig
            }
            groupInstanceIdConfig?.let { groupInstanceIdConfig ->
                it[ConsumerConfig.GROUP_INSTANCE_ID_CONFIG] = groupInstanceIdConfig
            }
            customConfig.forEach { (key, value) ->
                it[key] = value
            }
        }
    }
}
