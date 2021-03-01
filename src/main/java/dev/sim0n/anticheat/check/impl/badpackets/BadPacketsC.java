package dev.sim0n.anticheat.check.impl.badpackets;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketEntityAction;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

/**
 * You can't send more than 1 sprint/sneak update in a tick
 */
public class BadPacketsC extends PacketCheck {
    private boolean sprint, sneak;

    public BadPacketsC(PlayerData playerData) {
        super(playerData, "Bad Packets C", new ViolationHandler(2, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (movementTracker.isTeleporting())
            sprint = sneak = false;

        if (packet instanceof CPacketEntityAction) {
            switch (((CPacketEntityAction) packet).getAction()) {
                case START_SPRINTING:
                case STOP_SPRINTING:
                    if (sprint)
                        handleViolation(new DetailedPlayerViolation(this, "Sprint"));

                    sprint = true;
                    break;

                case START_SNEAKING:
                case STOP_SNEAKING:
                    if (sneak)
                        handleViolation(new DetailedPlayerViolation(this, "Sneak"));

                    sneak = true;
                    break;
            }
        } else if (packet instanceof CPacketPlayer) {
            sprint = sneak = false;
        }
    }
}
