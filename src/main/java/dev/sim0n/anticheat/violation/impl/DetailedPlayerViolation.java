package dev.sim0n.anticheat.violation.impl;

import dev.sim0n.anticheat.check.Check;
import lombok.Getter;

@Getter
public class DetailedPlayerViolation extends PlayerViolation {
    private final String data;

    public DetailedPlayerViolation(Check check, int level, Object data) {
        super(check, level);

        this.data = String.valueOf(data);
    }

    public DetailedPlayerViolation(Check check, Object data) {
        super(check, 1);

        this.data = String.valueOf(data);
    }
}
