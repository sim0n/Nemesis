package dev.sim0n.anticheat.check.impl.killaura;

import dev.sim0n.anticheat.check.base.packet.PostActionCheck;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketUseEntity;
import dev.sim0n.anticheat.player.PlayerData;

public class KillAuraB extends PostActionCheck {
    public KillAuraB(PlayerData playerData) {
        super(playerData, "Kill Aura B",
                packet -> packet instanceof CPacketUseEntity && ((CPacketUseEntity) packet).getAction() == CPacketUseEntity.EnumEntityUseAction.ATTACK);
    }
}
