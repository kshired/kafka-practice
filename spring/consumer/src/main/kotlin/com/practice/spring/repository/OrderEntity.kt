package com.practice.spring.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @Column(name = "ord_id")
    val orderId: String,

    @Column(name = "shop_id")
    val shopId: String,

    @Column(name = "menu_name")
    val menuName: String,

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "address")
    val address: String,

    @Column(name = "order_time")
    val orderTime: LocalDateTime
)
