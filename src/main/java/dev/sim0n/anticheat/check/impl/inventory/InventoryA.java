package dev.sim0n.anticheat.check.impl.inventory;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketClickWindow;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.PlayerViolation;

/**
 * This checks if you're clicking in your inventory but haven't opened it
 */
public class InventoryA extends PacketCheck {
    public InventoryA(PlayerData playerData) {
        super(playerData, "Inventory A", new ViolationHandler(5, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketClickWindow) {
            // 0 = player inventory
            if (((CPacketClickWindow) packet).getWindowId() == 0 && !actionTracker.isInventoryOpen()) {
                handleViolation(new PlayerViolation(this));
            }
        }
    }
}
