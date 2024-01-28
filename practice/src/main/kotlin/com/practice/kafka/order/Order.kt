package com.practice.kafka.order

import java.time.LocalDateTime

data class Order(
    val orderId: String,
    val shopId: String,
    val menuName: String,
    val userName: String,
    val phoneNumber: String,
    val address: String,
    val localDateTime: LocalDateTime
)
