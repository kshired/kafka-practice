package com.practice.kafka.event

interface EventHandler {
    fun onMessage(messageEvent: MessageEvent)
}
