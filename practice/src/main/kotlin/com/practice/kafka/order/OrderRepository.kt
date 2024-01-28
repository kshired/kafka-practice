package com.practice.kafka.order

interface OrderRepository {
    fun save(order: Order)
    fun saveAll(orders: List<Order>)
    fun close()
}
