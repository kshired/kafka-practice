package com.practice.spring.api.controller.request

import com.practice.spring.kafka.event.OrderEvent
import java.time.LocalDateTime

data class OrderRequest(
    val orderId: String,
    val shopId: String,
    val menuName: String,
    val userName: String,
    val phoneNumber: String,
    val address: String
) {
    fun toOrderEvent() = OrderEvent(
        orderId = orderId,
        shopId = shopId,
        menuName = menuName,
        userName = userName,
        phoneNumber = phoneNumber,
        address = address,
        orderTime = LocalDateTime.now()
    )
}
