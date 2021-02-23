package dev.sim0n.anticheat.check.impl.killaura;

import dev.sim0n.anticheat.check.base.packet.order.PacketOrderCheck;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketAnimation;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketUseEntity;
import dev.sim0n.anticheat.player.PlayerData;

public class KillAuraA extends PacketOrderCheck {
    public KillAuraA(PlayerData playerData) {
        super(playerData, "Kill Aura A",
                packet -> packet instanceof CPacketUseEntity && ((CPacketUseEntity) packet).getAction() == CPacketUseEntity.EnumEntityUseAction.ATTACK,
                packet -> packet instanceof CPacketAnimation);
    }
}
