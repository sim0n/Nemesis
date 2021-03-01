package dev.sim0n.anticheat.check.impl.aimassist;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

/**
 * This will detect clients trying to move in rounded moves, very old clients may do this
 */
public class AimAssistA extends RotationCheck {
    private float suspiciousYaw;

    public AimAssistA(PlayerData playerData) {
        super(playerData, "Aim Assist A", new ViolationHandler(20, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        if (actionTracker.getLastAttack() > 20 * 60)
            return;

        float yawChange = Math.abs(to.getYaw() - from.getYaw());

        if (yawChange > 1F && Math.round(yawChange) == yawChange && yawChange % 1.5F != 0F) {
            if (yawChange == suspiciousYaw) {
                handleViolation(new DetailedPlayerViolation(this, "Y " + yawChange));
            }

            suspiciousYaw = Math.round(yawChange);
        } else {
            suspiciousYaw = 0F;
        }
    }
}
