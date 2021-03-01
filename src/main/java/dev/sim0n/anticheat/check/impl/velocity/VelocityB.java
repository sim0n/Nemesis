package dev.sim0n.anticheat.check.impl.velocity;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.util.Vector;

/**
 * This detects vertical velocity modification
 */
public class VelocityB extends PositionCheck {
    public VelocityB(PlayerData playerData) {
        super(playerData, "Velocity B", new ViolationHandler(9, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        Vector currentVelocity = movementTracker.getCurrentVelocity();
        if (currentVelocity == null)
            return;

        double offsetY = to.getY() - from.getY();
        double velocityY = currentVelocity.getY();

        double ratio = offsetY / velocityY;

        if (from.isOnGround() && !to.isOnGround() && !data.isCollidedVertically()) {
            if (ratio < 0.995 && offsetY != JUMP_MOMENTUM) { // We want to ignore them if they're jumping
                if (++vl > 2) {
                    handleViolation(new DetailedPlayerViolation(this, ratio));
                }
            } else {
                decreaseVl(0.1);
            }
        } else {
            decreaseVl(0.05);
        }
    }
}