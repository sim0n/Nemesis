package dev.sim0n.anticheat.log;

import lombok.Getter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class LogManager {
    private final Queue<Log> queuedLogs = new ConcurrentLinkedQueue<>();
}
