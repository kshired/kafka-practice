package com.practice.kafka.producer

import com.github.javafaker.Faker
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Random


class FileUtilAppend {
    private fun getRandomValueFromList(list: List<String>, random: Random): String {
        val size = list.size
        val index: Int = random.nextInt(size)
        return list[index]
    }

    private fun produceMsg(faker: Faker, random: Random, id: Int): Map<String, String> {
        val shopId = getRandomValueFromList(pizzaShop, random)
        val pizzaName = getRandomValueFromList(pizzaNames, random)
        val ordId = "ord$id"
        val customerName = faker.name().fullName()
        val phoneNumber = faker.phoneNumber().phoneNumber()
        val address = faker.address().streetAddress()
        val now = LocalDateTime.now()
        val message = "$ordId, $shopId, $pizzaName, $customerName, $phoneNumber, $address, ${now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREAN))}"

        return mapOf(
            "key" to shopId,
            "message" to message
        )
    }

    fun writeMessage(filePath: String, faker: Faker, random: Random) {
        runCatching {
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            val printWriter = PrintWriter(bufferedWriter)
            for (i in 0..49) {
                val message = produceMsg(faker, random, orderSeq++)
                printWriter.println(message["key"] + "," + message["message"])
            }
            printWriter.close()
        }.onFailure {
            logger.error(it) { it.message }
        }
    }

    companion object {
        private val pizzaNames = listOf(
            "Potato Pizza", "Cheese Pizza",
            "Cheese Garlic Pizza", "Super Supreme", "Peperoni"
        )

        private val pizzaShop = listOf(
            "A001", "B001", "C001",
            "D001", "E001", "F001", "G001", "H001", "I001", "J001", "K001", "L001", "M001", "N001",
            "O001", "P001", "Q001"
        )
        private var orderSeq = 5000
    }
}

private val logger = KotlinLogging.logger {}

fun main() {
    val fileUtilAppend = FileUtilAppend()
    val seed: Long = 2022
    val random = Random(seed)
    val faker: Faker = Faker.instance(random)

    val resourceUrl = ClassLoader.getSystemClassLoader().getResource("pizza_append.txt")

    for (i in 0..999) {
        fileUtilAppend.writeMessage(resourceUrl!!.path, faker, random)
        logger.info { "###### iteration:$i file write is done" }
        runCatching {
            Thread.sleep(500)
        }.onFailure {
            logger.error(it) { it.message }
        }
    }
}
