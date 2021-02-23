package dev.sim0n.anticheat.check.impl.aimassist;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

public class AimAssistA extends RotationCheck {
    private float suspiciousYaw;

    public AimAssistA(PlayerData playerData) {
        super(playerData, "Aim Assist A", new ViolationHandler(20, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        if (actionTracker.getLastAttack() > 20 * 60)
            return;

        float diffYaw = Math.abs(to.getYaw() - from.getYaw());

        if (diffYaw > 1F && Math.round(diffYaw) == diffYaw && diffYaw % 1.5F != 0F) {
            if (diffYaw == suspiciousYaw) {
                handleViolation(new DetailedPlayerViolation(this, 1, "Y " + diffYaw));
            }

            suspiciousYaw = Math.round(diffYaw);
        } else {
            suspiciousYaw = 0F;
        }
    }
}
