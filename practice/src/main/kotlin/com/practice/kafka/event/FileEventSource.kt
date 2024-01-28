package com.practice.kafka.event

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.io.RandomAccessFile
import kotlin.time.Duration

class FileEventSource(
    private val eventHandler: EventHandler,
    private val updateInterval: Duration,
    private val file: File,
): Runnable {
    private var keepRunning = true
    private var filePointer = 0L
    private val logger = KotlinLogging.logger {}

    override fun run() {
        runCatching {
            while (keepRunning) {
                Thread.sleep(updateInterval.inWholeMilliseconds)
                val len = file.length()

                if (len > filePointer) {
                    logger.info { "File was updated" }
                    readAppendAndSend()
                } else if (len < filePointer) {
                    logger.info { "File was reset as filePointer is longer than file length" }
                    filePointer = 0
                } else {
                    logger.info { "File was not updated" }
                }
            }
        }.onFailure {
            logger.error(it) { "Error occurred while reading file" }
        }
    }

    private fun readAppendAndSend() {
        val raf = RandomAccessFile(file, "r")
        raf.seek(filePointer)

        while (true) {
            val line = raf.readLine() ?: break
            val (key, value) = line.split(delimiters = arrayOf(","), limit = 2)
            eventHandler.onMessage(MessageEvent(key, value))
        }

        filePointer = raf.length()
    }
}
