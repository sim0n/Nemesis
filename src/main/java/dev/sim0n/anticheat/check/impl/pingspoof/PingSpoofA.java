package dev.sim0n.anticheat.check.impl.pingspoof;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketKeepAlive;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.Bukkit;

import java.util.prefs.PreferenceChangeEvent;

public class PingSpoofA extends PacketCheck {
    private boolean sent;

    public PingSpoofA(PlayerData playerData) {
        super(playerData, "Ping Spoof A", new ViolationHandler(15, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketKeepAlive) {
            // We wait until next tick to make sure our transaction ping also updated
            sent = true;
        } else if (packet instanceof CPacketPlayer && sent) {
            long keepAlivePing = pingTracker.getKeepAlivePing();
            long transactionPing = pingTracker.getTransactionPing();

            // The keep alive ping is significantly higher than the transaction ping
            if (keepAlivePing - 100 > transactionPing) {
                handleViolation(new DetailedPlayerViolation(this, String.format("K %s T %s", keepAlivePing, transactionPing)));
            }

            sent = false;
        }
    }
}
