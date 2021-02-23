package dev.sim0n.anticheat.violation.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ViolationHandler {
    private final int maxViolations;
    private final long maxViolationTimeLength;

    public static final ViolationHandler NO_BAN = new ViolationHandler(Integer.MAX_VALUE, 0L);
    public static final ViolationHandler PACKET_ORDER_HANDLER = new ViolationHandler(2, 60000L);
}
