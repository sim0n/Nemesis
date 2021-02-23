package dev.sim0n.anticheat.violation.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ViolationLog {
    private final long timestamp = System.currentTimeMillis();

    private final int level;
}
