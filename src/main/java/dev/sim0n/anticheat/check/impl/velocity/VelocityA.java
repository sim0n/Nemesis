package dev.sim0n.anticheat.check.impl.velocity;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.PlayerViolation;

/**
 * This detects people not taking velocity at all
 */
public class VelocityA extends PositionCheck {
    public VelocityA(PlayerData playerData) {
        super(playerData, "Velocity A", new ViolationHandler(4, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        if (movementTracker.getCurrentVelocity() != null && !movementTracker.isTeleporting(5)) {
            if (to.getY() > from.getY() || data.isUnderBlock() || from.isOnGround() && !to.isOnGround()) {
                decreaseVl(0.1);
                return;
            }

            if (++vl > 1) {
                handleViolation(new PlayerViolation(this));
            }
        }
    }
}
