package com.example.kafka.consumer

import org.apache.kafka.clients.consumer.KafkaConsumer

class SimpleConsumer (
    kafkaConsumer: KafkaConsumer<String, String>
) : Consumer<String, String>(kafkaConsumer)
