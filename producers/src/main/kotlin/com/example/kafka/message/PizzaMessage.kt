package com.example.kafka.message

import com.github.javafaker.Faker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Random

class PizzaMessage {
    fun produceMessage(faker: Faker, random: Random, id: Int): Map<String, String> {
        val shopId = getRandomValueFromList(pizzaShops, random)
        val pizzaName = getRandomValueFromList(pizzaNames, random)

        val orderId = "ord$id"
        val customerName = faker.name().fullName()
        val phoneNumber = faker.phoneNumber().phoneNumber()
        val address = faker.address().fullAddress()
        val message =
            "order_id : $orderId, shop : $shopId, pizza_name : $pizzaName, customer_name : $customerName, phone_number : $phoneNumber, address : $address, time: ${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA))
            }"

        return mapOf(
            "key" to shopId,
            "message" to message
        )
    }

    private fun getRandomValueFromList(list: List<String>, random: Random): String {
        val size = list.size
        val index = random.nextInt(size)
        return list[index]
    }

    companion object {
        private val pizzaNames = listOf(
            "Potato Pizza", "Cheese Pizza", "Cheese Garlic Pizza",
            "Super Supreme", "Peperoni"
        )
        private val pizzaShops = listOf(
            "A001", "B001", "C001", "D001", "E001", "F001",
            "G001", "H001", "I001", "J001", "K001", "L001",
            "M001", "N001", "O001", "P001", "Q001"
        )
    }
}
