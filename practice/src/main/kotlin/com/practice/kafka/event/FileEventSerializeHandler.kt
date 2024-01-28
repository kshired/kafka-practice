package com.practice.kafka.event

import com.practice.kafka.order.Order
import com.practice.kafka.producer.Producer
import org.apache.kafka.clients.producer.KafkaProducer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileEventSerializeHandler(
    kafkaProducer: KafkaProducer<String, Order>,
    private val topic: String,
    private val sync: Boolean
) : Producer<String, Order>(kafkaProducer), EventHandler {
    override fun onMessage(messageEvent: MessageEvent) {
        val (key, value) = messageEvent
        if (sync) {
            sendSync(topic, key, value.toOrder())
        } else {
            sendAsync(topic, key, value.toOrder())
        }
    }

    private fun String.toOrder(): Order {
        val order = this.split(",").map { s -> s.trim() }
        return Order(
            orderId = order[0],
            shopId = order[1],
            menuName = order[2],
            userName = order[3],
            phoneNumber = order[4],
            address = order[5],
            localDateTime = LocalDateTime.parse(order[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }
}
