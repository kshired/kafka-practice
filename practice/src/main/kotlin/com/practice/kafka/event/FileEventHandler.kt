package com.practice.kafka.event

import com.practice.kafka.producer.Producer
import org.apache.kafka.clients.producer.KafkaProducer

class FileEventHandler(
    private val kafkaProducer: KafkaProducer<String, String>,
    private val topic: String,
    private val sync: Boolean
) : Producer<String, String>(kafkaProducer), EventHandler {
    override fun onMessage(messageEvent: MessageEvent) {
        val (key, value) = messageEvent
        if (sync) {
            sendSync(topic, key, value)
        } else {
            sendAsync(topic, key, value)
        }
    }
}
