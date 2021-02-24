package dev.sim0n.anticheat.check.impl.badpackets;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

/**
 * The client sends a position update every 20 ticks, if they don't do that then they're cheating
 */
public class BadPacketsA extends PacketCheck {
    private int streak;

    public BadPacketsA(PlayerData playerData) {
        super(playerData, "Bad Packets A", new ViolationHandler(4, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketPlayer) {
            if (((CPacketPlayer) packet).isMoving() || playerData.getBukkitPlayer().isInsideVehicle()) {
                streak = 0;
            } else if (++streak > 20) {
                handleViolation(new DetailedPlayerViolation(this, streak - 20, streak));
            }
        }
    }
}
