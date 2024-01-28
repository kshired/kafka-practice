package com.practice.kafka.event

data class MessageEvent(
    val key: String,
    val value: String
)
