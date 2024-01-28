package com.practice.kafka.producer

import com.practice.kafka.config.KafkaConfig

fun main() {
    val fileProducer = FileProducer(KafkaConfig().simpleProducer())
    fileProducer.produce()

    val fileAppendProducer = FileAppendProducer(KafkaConfig().simpleProducer())
    fileAppendProducer.produce()

    val fileAppendSerializeProducer = FileAppendSerializeProducer(KafkaConfig().orderSerializerProducer())
    fileAppendSerializeProducer.produce()
}
