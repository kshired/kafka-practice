package com.practice.kafka.order

import io.github.oshai.kotlinlogging.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Timestamp

class OrderRepositoryImpl : OrderRepository {
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private val sql = """
        INSERT INTO public.orders (ord_id, shop_id, menu_name, user_name, phone_number, address, order_time) 
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()
    private val logger = KotlinLogging.logger {}

    constructor(url: String, username: String, password: String) {
        runCatching {
            connection = DriverManager.getConnection(url, username, password)
            preparedStatement = connection.prepareStatement(sql)
        }.onFailure {
            logger.error(it) { it.message }
        }
    }

    override fun save(order: Order) {
        runCatching {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, order.orderId)
            preparedStatement.setString(2, order.shopId)
            preparedStatement.setString(3, order.menuName)
            preparedStatement.setString(4, order.userName)
            preparedStatement.setString(5, order.phoneNumber)
            preparedStatement.setString(6, order.address)
            preparedStatement.setTimestamp(7, Timestamp.valueOf(order.localDateTime))
            preparedStatement.executeUpdate()
        }.onFailure {
            logger.error(it) { it.message }
        }
    }

    override fun saveAll(orders: List<Order>) {
        runCatching {
            val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
            orders.forEach {
                preparedStatement.setString(1, it.orderId)
                preparedStatement.setString(2, it.shopId)
                preparedStatement.setString(3, it.menuName)
                preparedStatement.setString(4, it.userName)
                preparedStatement.setString(5, it.phoneNumber)
                preparedStatement.setString(6, it.address)
                preparedStatement.setTimestamp(7, Timestamp.valueOf(it.localDateTime))
                preparedStatement.addBatch()
                preparedStatement.clearParameters()
            }
            preparedStatement.executeBatch()
            preparedStatement.clearBatch()
        }.onFailure {
            logger.error(it) { it.message }
        }
    }

    override fun close() {
        runCatching {
            preparedStatement.close()
            connection.close()
            logger.info { "Successfully closed connection" }
        }.onFailure {
            logger.error(it) { it.message }
        }
    }
}
