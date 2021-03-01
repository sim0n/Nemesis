package dev.sim0n.anticheat.check.impl.speed;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.util.NmsUtil;
import dev.sim0n.anticheat.util.TrigUtil;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Speed extends PositionCheck {
    private static final Set<Integer> DIRECTIONS = new HashSet<>(Arrays.asList(45, 90, 135, 180));

    private double lastOffsetH;
    private float blockFriction;

    public Speed(PlayerData playerData) {
        super(playerData, "Speed", new ViolationHandler(12, 30000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        if (to.getX() != from.getX() || to.getZ() != from.getZ()) {
            Player player = playerData.getBukkitPlayer();
            EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(player);

            double offsetH = MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
            double movementSpeed = statusTracker.getMovementSpeed();

            float moveAngle = TrigUtil.getMoveAngle(from, to);

            if (from.isOnGround()) {
                // Since we don't have proper sprint accounting we'll just always assume they're sprinting
                movementSpeed *= SPRINT_BOOST;

                blockFriction *= 0.91F;
                movementSpeed *= LAND_MOVEMENT_FACTOR / Math.pow(blockFriction, 3);

                // They're moving in another direction
                if (moveAngle > 135)
                    movementSpeed /= 1.05;

                /*
                 * If you're not on ground and under a block or your deltaY is greater than or equal
                 * to the jump momentum then apply the jump sprint boost
                 */
                if (!to.isOnGround() && (data.isUnderBlock() || data.isWasUnderBlock() || to.getY() - from.getY() >= JUMP_MOMENTUM))
                    movementSpeed += JUMP_BOOST;
            } else {
                movementSpeed = JUMP_MOVEMENT_FACTOR;
                blockFriction = AIR_FRICTION;
            }

            Vector velocity = movementTracker.getCurrentVelocity();
            if (velocity != null)
                movementSpeed += MathUtil.hypot(velocity.getX(), velocity.getZ());

            double speedup = (offsetH - lastOffsetH) / movementSpeed;

            /*if (DIRECTIONS.stream().anyMatch(direction -> Math.abs(direction - moveAngle) < 0.0001F)) {
                // they're strafing, maybe do something about it or make another check for it
            }*/

            if (speedup > 1.001 && !movementTracker.isTeleporting()) {
                if (offsetH > 0.2) {
                    if (++vl > 1) {
                        handleViolation(new DetailedPlayerViolation(this, speedup));
                    }
                } else {
                    decreaseVl(0.001);
                }
            } else {
                decreaseVl(0.05);
            }

            lastOffsetH = offsetH * blockFriction;
            blockFriction = NmsUtil.getBlockFriction(entityPlayer.world, to.getX(), to.getY() - 1, to.getZ());
        }
    }
}
