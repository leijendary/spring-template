package com.leijendary.spring.microservicetemplate.worker;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

import static java.lang.String.format;
import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.time.Instant.now;

@Slf4j
public class SnowflakeIdWorker {

    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;
    private static final int MAX_NODE_ID = (int) (Math.pow(2, NODE_ID_BITS) - 1);
    private static final int MAX_SEQUENCE = (int) (Math.pow(2, SEQUENCE_BITS) - 1);

    // Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
    private static final long CUSTOM_EPOCH = 1420070400000L;

    private final long nodeId;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    // Create SnowflakeIdWorker with a nodeId
    public SnowflakeIdWorker(int nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
        }

        this.nodeId = nodeId;
    }

    // Let SnowflakeIdWorker generate a nodeId
    public SnowflakeIdWorker() {
        this.nodeId = createNodeId();
    }

    public synchronized long nextId() {
        var currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException(format("System clock is in the past, waiting until %s", currentTimestamp));
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;

            if (sequence == 0) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            // Reset sequence to start with zero for the next millisecond
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        var id = currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS);
        id |= (nodeId << SEQUENCE_BITS);
        id |= sequence;

        return id;
    }

    // Get current timestamp in milliseconds, adjust for the custom epoch.
    private static long timestamp() {
        return now().toEpochMilli() - CUSTOM_EPOCH;
    }

    // Block and wait till next millisecond
    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }

        return currentTimestamp;
    }

    private long createNodeId() {
        long nodeId;

        try {
            final var stringBuilder = new StringBuilder();
            final var networkInterfaces = getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                final var networkInterface = networkInterfaces.nextElement();
                final var mac = networkInterface.getHardwareAddress();

                if (mac != null) {
                    for (byte b : mac) {
                        stringBuilder.append(format("%02X", b));
                    }
                }
            }

            nodeId = stringBuilder.toString().hashCode();
        } catch (final Exception e) {
            log.error("Error when getting the network interfaces. Generating a random ID", e);

            nodeId = new SecureRandom().nextLong();
        }

        nodeId = nodeId & MAX_NODE_ID;

        return nodeId;
    }
}
