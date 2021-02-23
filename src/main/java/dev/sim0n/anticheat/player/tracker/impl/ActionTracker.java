package dev.sim0n.anticheat.player.tracker.impl;

import dev.sim0n.anticheat.net.packet.ServerBoundPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.*;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.util.PacketHelper;
import lombok.Getter;

@Getter
public class ActionTracker extends PlayerTracker {
    private int lastAttack;

    private boolean digging;
    private boolean inventoryOpen;

    public ActionTracker(PlayerData playerData) {
        super(playerData);

        registerIncomingPreHandler(CPacketClientStatus.class, packet -> {
            if (packet.getStatus() == CPacketClientStatus.State.OPEN_INVENTORY_ACHIEVEMENT) {
                inventoryOpen = true;
            }
        }, ServerBoundPacket.PLAY_CLIENT_COMMAND.getId());

        registerIncomingPreHandler(CPacketCloseWindow.class, packet -> {
            inventoryOpen = false;
        }, ServerBoundPacket.PLAY_WINDOW_CLOSE.getId());

        registerIncomingPreHandler(CPacketPlayerDigging.class, packet -> {
            switch (packet.getAction()) {
                case START_DESTROY_BLOCK:
                    digging = true;
                    break;

                case STOP_DESTROY_BLOCK:
                case ABORT_DESTROY_BLOCK:
                    digging = false;
                    break;
            }
        }, ServerBoundPacket.PLAY_BLOCK_DIG.getId());

        registerIncomingPreHandler(CPacketUseEntity.class, packet -> {
            if (packet.getAction() == CPacketUseEntity.EnumEntityUseAction.ATTACK) {
                lastAttack = 0;
            }
        }, ServerBoundPacket.PLAY_USE_ENTITY.getId());

        registerIncomingPostHandler(CPacketPlayer.class, packet -> {
            ++lastAttack;
        }, PacketHelper.FLYING_IDS);
    }


}
