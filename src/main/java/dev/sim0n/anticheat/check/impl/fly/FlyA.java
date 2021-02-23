package dev.sim0n.anticheat.check.impl.fly;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This checks the distance the player is from the last ground location
 */
public class FlyA extends PositionCheck {
    private static final double LIMIT = 1.83;

    private double lastGroundY;

    public FlyA(PlayerData playerData) {
        super(playerData, "Fly A", new ViolationHandler(20, 30000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        if (movementTracker.isTeleporting() || from.isOnGround() || to.isOnGround() || data.isOnLadder() || data.isInLiquid()) {
            lastGroundY = to.getY();
            return;
        }

        if (to.getY() <= from.getY())
            return;

        double difference = to.getY() - lastGroundY;

        AtomicReference<Double> limit = new AtomicReference<>(LIMIT);

        Optional.ofNullable(movementTracker.getVelocity())
                .map(Vector::getY)
                .ifPresent(velocity -> limit.updateAndGet(v -> v + Math.abs(velocity * 4)));

        int jumpAmplifier = statusTracker.getJumpBoostAmplifier();

        if (jumpAmplifier > 0)
            limit.updateAndGet(v -> v + Math.pow(jumpAmplifier + 4.2, 2D) / 16D);

        if (difference > limit.get()) {
            handleViolation(new DetailedPlayerViolation(this, (int) Math.ceil(difference - 1.1), difference + ", " + limit.get()));
        }
    }
}

