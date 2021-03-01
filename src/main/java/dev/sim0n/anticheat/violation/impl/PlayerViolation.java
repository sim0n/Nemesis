package dev.sim0n.anticheat.violation.impl;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.violation.base.AbstractPlayerViolation;
import lombok.Getter;

@Getter
public class PlayerViolation extends AbstractPlayerViolation {
    private final int level;

    public PlayerViolation(Check check, int level) {
        super(check);

        this.level = level;
    }

    public PlayerViolation(Check check) {
        super(check);

        this.level = 1;
    }

}
