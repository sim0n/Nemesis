package dev.sim0n.anticheat.check.impl.fly;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

public class FlyB extends PositionCheck {
    private static final double TOLERANCE = 0.005;

    private Double lastOffsetY;

    public FlyB(PlayerData playerData) {
        super(playerData, "Fly B", new ViolationHandler(10, 30000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        if (movementTracker.getVelocityV() > 5 || movementTracker.isTeleporting() || data.isOnLadder()) {
            lastOffsetY = null;
            return;
        }

        double offsetY = to.getY() - from.getY();

        if (!from.isOnGround() && !to.isOnGround()) {
            if (lastOffsetY != null && !data.isInLiquid()) {
                double expectedOffsetY = (lastOffsetY - WORLD_GRAVITY) * VERTICAL_AIR_FRICTION;

                // We're going to ignore them if they're in an unloaded chunk
                if (offsetY + 0.09800000190734881 <= 0.001) {
                    lastOffsetY = null;
                    return;
                }

                double difference = Math.abs(expectedOffsetY - offsetY);

                // Since we don't have any direct calculation for vertical collision we'll just make it a bit more lenient
                int limit = data.isUnderBlock() || data.isWasUnderBlock() ? 3 : 1;

                if (difference > TOLERANCE) {
                    if (++vl > limit) {
                        handleViolation(new DetailedPlayerViolation(this, String.format("%s -> %s", offsetY, expectedOffsetY)));
                    }
                } else {
                    decreaseVl(0.45);
                }
            }

            lastOffsetY = offsetY;
        } else {
            lastOffsetY = null;
        }
    }
}
