package com.practice.spring.kafka.config

import com.practice.spring.kafka.event.OrderEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConsumerConfig(
    private val kafkaConfigProperties: KafkaConfigProperties
) {
    private val logger = KotlinLogging.logger {}

    @Bean
    fun orderEventContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderEvent> {
        val deserializer = JsonDeserializer<OrderEvent>()
        deserializer.addTrustedPackages("*")
        deserializer.setRemoveTypeHeaders(false)
        deserializer.setUseTypeMapperForKey(true)

        return ConcurrentKafkaListenerContainerFactory<String, OrderEvent>()
            .apply {
                consumerFactory = consumerFactory(deserializer)
                setCommonErrorHandler(errorHandler())
            }
    }

    @Bean
    fun kafkaAdmin(): KafkaAdmin? = null

    private fun <T> consumerFactory(deserializer: JsonDeserializer<T>): ConsumerFactory<String, T> {
        val config = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigProperties.bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS to StringSerializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to deserializer::class.java,
        )
        return DefaultKafkaConsumerFactory(config, StringDeserializer(), deserializer)
    }

    private fun errorHandler(): DefaultErrorHandler {
        return DefaultErrorHandler({ record, execption ->
            logger.error(execption) { "Error occurred while processing: ${record.value()}" }
        }, FixedBackOff(1000, 2))
    }
}
