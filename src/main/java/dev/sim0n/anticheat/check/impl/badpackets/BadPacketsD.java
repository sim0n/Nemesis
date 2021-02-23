package dev.sim0n.anticheat.check.impl.badpackets;

import dev.sim0n.anticheat.check.base.packet.order.InversePacketOrderCheck;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketBlockPlacement;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayerDigging;
import dev.sim0n.anticheat.player.PlayerData;

public class BadPacketsD extends InversePacketOrderCheck {
    public BadPacketsD(PlayerData playerData) {
        super(playerData, "Bad Packets D",
                packet -> packet instanceof CPacketPlayerDigging && ((CPacketPlayerDigging) packet).getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                packet -> packet instanceof CPacketBlockPlacement);
    }
}
