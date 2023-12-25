package com.example.kafka.partitioner

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.utils.Utils

class CustomPartitioner : Partitioner {
    private val logger = KotlinLogging.logger {}
    private lateinit var specialKeyName: String

    override fun configure(configs: MutableMap<String, *>) {
        specialKeyName = configs["custom.specialKey"] as String
    }

    override fun close() {
        logger.info { "Closing partitioner" }
    }

    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?
    ): Int {
        val partitionInfos = cluster?.partitionsForTopic(topic)
        val partitionSize = partitionInfos?.size ?: 0
        val specialPartitionSize = partitionSize / 2

        require(!(keyBytes == null || valueBytes == null)) { "key and value must not be null" }

        val partitionIdx = if (key == specialKeyName) {
            Utils.toPositive(Utils.murmur2(valueBytes)) % specialPartitionSize
        } else {
            Utils.toPositive(Utils.murmur2(keyBytes)) % (partitionSize - specialPartitionSize) + specialPartitionSize
        }

        logger.info { "key : $key, value : $value, partitionIdx : $partitionIdx" }

        return partitionIdx
    }
}
