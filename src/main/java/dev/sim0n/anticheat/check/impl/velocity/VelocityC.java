package dev.sim0n.anticheat.check.impl.velocity;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.util.Vector;

/**
 * This detects horizontal velocity
 */
public class VelocityC extends PositionCheck {
    public VelocityC(PlayerData playerData) {
        super(playerData, "Velocity C", new ViolationHandler(15, 60000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        Vector currentVelocity = movementTracker.getCurrentVelocity();
        if (currentVelocity == null || movementTracker.isTeleporting(5))
            return;

        CustomLocation lastLocation = movementTracker.getLastLastLocation();

        double lastOffsetH = MathUtil.hypot(lastLocation.getX() - to.getX(), lastLocation.getZ() - to.getZ());
        double offsetH = MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());

        double velocityH = MathUtil.hypot(currentVelocity.getX(), currentVelocity.getZ());

        // Account for attack slowdown
        if (actionTracker.getLastAttack() <= 1)
            velocityH *= 0.6;

        // Since this check isn't very good we check the last location as well
        double ratio = Math.max(offsetH / velocityH, lastOffsetH / velocityH);

        if (ratio < 0.61 && !data.isCollidedHorizontally()) {
            if (++vl > 4) {
                handleViolation(new DetailedPlayerViolation(this, ratio));
            }
        } else {
            decreaseVl(1);
        }
    }
}
