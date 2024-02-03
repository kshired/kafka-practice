package com.practice.spring.kafka.config

import com.practice.spring.kafka.event.OrderEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerTemplates {
    @Bean
    fun orderKafkaTemplate(
        @Qualifier("defaultProducerFactory") producerFactory: ProducerFactory<String, OrderEvent>
    ): KafkaTemplate<String, OrderEvent> {
        return KafkaTemplate(producerFactory)
    }
}
