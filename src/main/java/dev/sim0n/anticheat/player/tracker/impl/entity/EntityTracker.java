package dev.sim0n.anticheat.player.tracker.impl.entity;

import dev.sim0n.anticheat.net.packet.ClientBoundPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketDestroyEntities;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketEntity;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketEntityTeleport;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketNamedEntity;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.util.PacketHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.*;

@Getter
public class EntityTracker extends PlayerTracker {
    private final Int2ObjectMap<TrackedEntity> trackedEntities = new Int2ObjectArrayMap<>();

    public EntityTracker(PlayerData playerData) {
        super(playerData);

        registerOutgoingPreHandler(SPacketEntity.class, packet -> {
            int entityId = packet.getEntityId();

            TrackedEntity entity = trackedEntities.get(entityId);

            if (entity == null)
                return;

            double x = (double) packet.getPosX() / 32D;
            double y = (double) packet.getPosY() / 32D;
            double z = (double) packet.getPosZ() / 32D;

            entity.handleRelMove(x, y, z);
        }, PacketHelper.ENTITY_IDS);

        registerOutgoingPreHandler(SPacketEntityTeleport.class, packet -> {
            int entityId = packet.getEntityId();

            TrackedEntity entity = trackedEntities.get(entityId);

            if (entity == null)
                return;

            double x = (double) packet.getPosX() / 32D;
            double y = (double) packet.getPosY() / 32D;
            double z = (double) packet.getPosZ() / 32D;

            entity.addPosition(new Vector(x, y, z));
        }, ClientBoundPacket.PLAY_ENTITY_TELEPORT.getId());

        registerOutgoingPreHandler(SPacketNamedEntity.class, packet -> {
            int entityId = packet.getEntityId();

            TrackedEntity entity;

            // For whatever reason Int2ObjectMap#computeIfAbsent throws an error so we have to do this instead
            if (trackedEntities.containsKey(entityId)) {
                entity = trackedEntities.get(entityId);
            } else {
                trackedEntities.put(entityId, entity = new TrackedEntity());
            }

            double x = (double) packet.getPosX() / 32D;
            double y = (double) packet.getPosY() / 32D;
            double z = (double) packet.getPosZ() / 32D;

            entity.addPosition(new Vector(x, y, z));
        }, ClientBoundPacket.PLAY_SPAWN_NAMED.getId());

        registerOutgoingPreHandler(SPacketDestroyEntities.class, packet -> {
            Arrays.stream(packet.getIds()).forEach(trackedEntities::remove);
        }, ClientBoundPacket.PLAY_ENTITY_DESTROY.getId());
    }

    @Getter
    public static class TrackedEntity {
        private final Deque<TrackedPosition> trackedPositions = new LinkedList<>();

        public void addPosition(Vector position) {
            trackedPositions.add(new TrackedPosition(position));

            if (trackedPositions.size() > 20)
                trackedPositions.removeFirst();
        }

        public void handleRelMove(double x, double y, double z) {
            addPosition(trackedPositions.peekLast().getPosition().clone().add(new Vector(x, y, z)));
        }

        public List<TrackedPosition> getPositions(long ping) {
            List<TrackedPosition> positions = new ArrayList<>();

            long now = System.currentTimeMillis();

            long offset = now - ping - 200L;

            trackedPositions.stream()
                    .filter(position -> position.getTimestamp() - offset > 0L)
                    .forEach(positions::add);

            // if there's no positions added, add the most recent one
            if (positions.isEmpty())
                positions.add(trackedPositions.peekLast());

            return positions;
        }
    }
}
