package dev.sim0n.anticheat.check.impl.inventory;

import dev.sim0n.anticheat.check.base.packet.PostActionCheck;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketClickWindow;
import dev.sim0n.anticheat.player.PlayerData;

public class InventoryB extends PostActionCheck {
    public InventoryB(PlayerData playerData) {
        super(playerData, "Inventory B",
                packet -> packet instanceof CPacketClickWindow);
    }
}
