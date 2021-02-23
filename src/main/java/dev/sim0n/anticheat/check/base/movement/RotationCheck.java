package dev.sim0n.anticheat.check.base.movement;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

public abstract class RotationCheck extends Check {
    public RotationCheck(PlayerData playerData, String name, ViolationHandler violationHandler) {
        super(playerData, name, violationHandler);
    }

    public abstract void handle(CustomLocation to, CustomLocation from);
}
