package com.example.kafka.producer

interface Producer<T, U> {
    fun send(topic: String, key: T, value: U)
}
