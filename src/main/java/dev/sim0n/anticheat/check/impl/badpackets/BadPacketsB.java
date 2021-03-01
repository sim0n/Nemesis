package dev.sim0n.anticheat.check.impl.badpackets;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

/**
 * Minecraft limits your pitch to 90, if it's greater than this or less than -90 they're cheating
 */
public class BadPacketsB extends RotationCheck {
    public BadPacketsB(PlayerData playerData) {
        super(playerData, "Bad Packets B", new ViolationHandler(1, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        // Pitch can go above 90 if you're teleporting
        if (movementTracker.isTeleporting())
            return;

        float pitch = Math.abs(to.getPitch());

        if (pitch > 90F)
            handleViolation(new DetailedPlayerViolation(this, pitch));
    }
}
