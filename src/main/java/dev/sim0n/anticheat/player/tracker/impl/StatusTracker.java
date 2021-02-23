package dev.sim0n.anticheat.player.tracker.impl;

import dev.sim0n.anticheat.net.packet.ClientBoundPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketEntityEffect;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketRemoveEntityEffect;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketUpdateAttributes;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.util.NmsUtil;
import lombok.Getter;

@Getter
public class StatusTracker extends PlayerTracker {
    private double movementSpeed;

    private int jumpBoostAmplifier;
    private int speedBoostAmplifier;

    public StatusTracker(PlayerData playerData) {
        super(playerData);

        registerOutgoingPreHandler(SPacketUpdateAttributes.class, packet -> {
            if (packet.getEntityId() != playerData.getEntityId())
                return;

            packet.getSnapshots().stream()
                    .filter(snapshot -> snapshot.getName().equals("generic.movementSpeed"))
                    .findFirst()
                    .ifPresent(snapshot -> {
                        playerData.sendTransaction((uid) -> movementSpeed = NmsUtil.getMovementSpeed(playerData.getBukkitPlayer()));
                    });
        }, ClientBoundPacket.PLAY_ENTITY_ATTRIBUTES.getId());

        registerOutgoingPreHandler(SPacketEntityEffect.class, packet -> {
            if (packet.getEntityId() != playerData.getEntityId())
                return;

            int amplifier = packet.getAmplifier();

            playerData.sendTransaction((uid) -> {
                switch (packet.getEffectId()) {
                    case 1:
                        speedBoostAmplifier = amplifier;
                        break;

                    case 8:
                        jumpBoostAmplifier = amplifier;
                        break;
                }
            });
        }, ClientBoundPacket.PLAY_ENTITY_EFFECT_ADD.getId());

        registerOutgoingPreHandler(SPacketRemoveEntityEffect.class, packet -> {
            if (packet.getEntityId() != playerData.getEntityId())
                return;

            playerData.sendTransaction((uid) -> {
                switch (packet.getEffectId()) {
                    case 1:
                        speedBoostAmplifier = 0;
                        break;

                    case 8:
                        jumpBoostAmplifier = 0;
                        break;
                }
            });
        }, ClientBoundPacket.PLAY_ENTITY_EFFECT_REMOVE.getId());
    }
}
