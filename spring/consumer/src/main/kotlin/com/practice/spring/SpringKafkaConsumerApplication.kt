package com.practice.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SpringKafkaConsumerApplication

fun main(args: Array<String>) {
    runApplication<SpringKafkaConsumerApplication>(*args)
}
