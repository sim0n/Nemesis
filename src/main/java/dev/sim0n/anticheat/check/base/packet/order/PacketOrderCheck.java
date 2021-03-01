package dev.sim0n.anticheat.check.base.packet.order;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.PlayerViolation;

import java.util.function.Predicate;

public abstract class PacketOrderCheck extends PacketCheck {
    private boolean sent;

    private final Predicate<EPacket<?>> first;
    private final Predicate<EPacket<?>> second;

    public PacketOrderCheck(PlayerData playerData, String name, Predicate<EPacket<?>> first, Predicate<EPacket<?>> second) {
        super(playerData, name, ViolationHandler.PACKET_ORDER_HANDLER);

        this.first = first;
        this.second = second;
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (movementTracker.isTeleporting()) {
            sent = true;
            return;
        }

        if (first.test(packet)) {
            if (sent)
                return;

            handleViolation(new PlayerViolation(this));
        } else if (second.test(packet)) {
            sent = true;
        } else if (packet instanceof CPacketPlayer) {
            sent = false;
        }
    }
}
