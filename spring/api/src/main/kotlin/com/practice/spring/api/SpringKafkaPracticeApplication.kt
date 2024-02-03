package com.practice.spring.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SpringKafkaPracticeApplication

fun main(args: Array<String>) {
    runApplication<SpringKafkaPracticeApplication>(*args)
}
