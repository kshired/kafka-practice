package com.practice.spring.kafka.event

import java.time.LocalDateTime

data class OrderEvent(
    val orderId: String,
    val shopId: String,
    val menuName: String,
    val userName: String,
    val phoneNumber: String,
    val address: String,
    val orderTime: LocalDateTime
)
