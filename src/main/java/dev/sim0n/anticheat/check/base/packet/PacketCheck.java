package dev.sim0n.anticheat.check.base.packet;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

public abstract class PacketCheck extends Check {
    public PacketCheck(PlayerData playerData, String name, ViolationHandler violationHandler) {
        super(playerData, name, violationHandler);
    }

    public abstract void handle(EPacket<?> packet);
}
