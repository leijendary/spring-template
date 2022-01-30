package com.leijendary.spring.boot.template.core.worker

import com.leijendary.spring.boot.template.core.util.logger
import java.net.NetworkInterface
import java.net.NetworkInterface.getNetworkInterfaces
import java.security.SecureRandom
import java.time.Instant.now
import java.util.*
import kotlin.math.pow

class SnowflakeIdWorker {
    private val log = logger()
    private val nodeId: Long

    @Volatile
    private var lastTimestamp = -1L

    @Volatile
    private var sequence = 0L

    companion object {
        private const val NODE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12
        private val MAX_NODE_ID = (2.0.pow(NODE_ID_BITS.toDouble()) - 1).toInt()
        private val MAX_SEQUENCE = (2.0.pow(SEQUENCE_BITS.toDouble()) - 1).toInt()

        // Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
        private const val CUSTOM_EPOCH = 1420070400000L

        // Get current timestamp in milliseconds, adjust for the custom epoch.
        private fun timestamp(): Long {
            return now().toEpochMilli() - CUSTOM_EPOCH
        }
    }

    // Create SnowflakeIdWorker with a nodeId
    constructor(nodeId: Int) {
        require(!(nodeId < 0 || nodeId > MAX_NODE_ID)) {
            String.format(
                "NodeId must be between %d and %d",
                0,
                MAX_NODE_ID
            )
        }
        this.nodeId = nodeId.toLong()
    }

    // Let SnowflakeIdWorker generate a nodeId
    constructor() {
        nodeId = createNodeId()
    }

    @Synchronized
    fun nextId(): Long {
        var currentTimestamp = timestamp()

        check(currentTimestamp >= lastTimestamp) {
            String.format(
                "System clock is in the past, waiting until %s",
                lastTimestamp
            )
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = sequence + 1 and MAX_SEQUENCE.toLong()

            if (sequence == 0L) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            // Reset sequence to start with zero for the next millisecond
            sequence = 0
        }

        lastTimestamp = currentTimestamp

        var id = currentTimestamp shl NODE_ID_BITS + SEQUENCE_BITS
        id = id or (nodeId shl SEQUENCE_BITS)
        id = id or sequence

        return id
    }

    // Block and wait till next millisecond
    private fun waitNextMillis(currentTimestamp: Long): Long {
        var timestamp = currentTimestamp

        while (timestamp == lastTimestamp) {
            timestamp = timestamp()
        }

        return timestamp
    }

    private fun createNodeId(): Long {
        var nodeId: Long = try {
            val stringBuilder = StringBuilder()
            val networkInterfaces: Enumeration<NetworkInterface> = getNetworkInterfaces()

            while (networkInterfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = networkInterfaces.nextElement()
                val mac = networkInterface.hardwareAddress

                if (mac != null) {
                    for (b in mac) {
                        stringBuilder.append(String.format("%02X", b))
                    }
                }
            }

            stringBuilder.toString().hashCode().toLong()
        } catch (e: Exception) {
            log.error("Error when getting the network interfaces. Generating a random ID", e)

            SecureRandom().nextLong()
        }

        nodeId = nodeId and MAX_NODE_ID.toLong()

        return nodeId
    }
}