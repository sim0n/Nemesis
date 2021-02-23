package dev.sim0n.anticheat.violation.base;

import dev.sim0n.anticheat.check.Check;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPlayerViolation implements Violation {
    private final Check check;
}
