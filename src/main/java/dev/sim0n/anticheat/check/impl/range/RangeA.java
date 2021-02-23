package dev.sim0n.anticheat.check.impl.range;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketUseEntity;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.entity.EntityTracker;
import dev.sim0n.anticheat.player.tracker.impl.entity.TrackedPosition;
import dev.sim0n.anticheat.util.data.BoundingBox;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.util.Vector;

import java.util.List;

public class RangeA extends PacketCheck {
    private static final double MAX_REACH_RANGE = 3.5;

    private List<TrackedPosition> positions;

    public RangeA(PlayerData playerData) {
        super(playerData, "Range A", new ViolationHandler(8, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketPlayer) {
            if (positions != null) {
                CustomLocation location = movementTracker.getLastLocation();

                // Go through each position and get the minimum reach range
                positions.stream()
                        .mapToDouble(position -> getReachRange(location, position.getPosition()))
                        .min()
                        .ifPresent(range -> {
                            range = Math.sqrt(range);

                            if (range > MAX_REACH_RANGE && range < 6.5) {
                                double difference = range - MAX_REACH_RANGE;

                                handleViolation(new DetailedPlayerViolation(this, (int) Math.ceil(difference * 2), range));
                            }
                        });

                positions = null;
            }
        } else if (packet instanceof CPacketUseEntity && ((CPacketUseEntity) packet).getAction() == CPacketUseEntity.EnumEntityUseAction.ATTACK) {
            int entityId = ((CPacketUseEntity) packet).getEntityId();

            EntityTracker.TrackedEntity trackedEntity = playerData.getEntityTracker().getTrackedEntities().get(entityId);

            if (trackedEntity == null)
                return;

            positions = trackedEntity.getPositions(pingTracker.getTransactionPing());
        }
    }

    /**
     * Calculates the reach range between {@param playerLocation} and {@param targetLocation}
     * @param playerLocation The player's location
     * @param targetLocation The target's location
     * @return The reach range
     */
    public double getReachRange(CustomLocation playerLocation, Vector targetLocation) {
        BoundingBox bb = new BoundingBox(targetLocation, 0.403125, 1.905, 0.1001);

        return bb.distance(playerLocation);
    }
}
