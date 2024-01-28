package com.practice.kafka.consumer

import com.practice.kafka.order.Order
import com.practice.kafka.order.OrderRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileToDbConsumer(
    kafkaConsumer: KafkaConsumer<String, String>,
    private val orderRepository: OrderRepository
) : BaseConsumer<String, String>(kafkaConsumer, false) {
    private val logger = KotlinLogging.logger {}

    override fun consume(topics: List<String>) {
        consume(topics) { records ->
            orderRepository.saveAll(
                records.map {
                    val order = it.value().split(",").map { s -> s.trim() }
                    Order(
                        orderId = order[0],
                        shopId = order[1],
                        menuName = order[2],
                        userName = order[3],
                        phoneNumber = order[4],
                        address = order[5],
                        localDateTime = LocalDateTime.parse(order[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            )
            records.forEach {
                logger.info { "Save key : ${it.key()}, value : ${it.value()}, partition : ${it.partition()}, offset : ${it.offset()}" }
            }
        }
        orderRepository.close()
    }
}
