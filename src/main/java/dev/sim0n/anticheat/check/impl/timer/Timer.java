package dev.sim0n.anticheat.check.impl.timer;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketPosLook;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import org.bukkit.Bukkit;

/**
 * The minecraft client will retain a balance of 50ms between each player packet.
 * We check if their balance is off by 50
 */
public class Timer extends PacketCheck {
    private Long lastPlayerPacketTime;

    private long balance = -100L;

    public Timer(PlayerData playerData) {
        super(playerData, "Timer", new ViolationHandler(15, 60000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketPlayer && pingTracker.isAcceptedTransaction() && pingTracker.isAcceptedKeepAlive()) {
            long now = packet.getTimestamp();

            if (lastPlayerPacketTime != null) {
                long delay = now - lastPlayerPacketTime;

                balance += 50L - delay;

                if (balance > 50) {
                    if (++vl > 4) {
                        double rate = Math.min(50D / delay, 10D);

                        // We want to increase the violation level depending on packet rate
                        int level = (int) Math.ceil((rate - 1) * 8);

                        handleViolation(new DetailedPlayerViolation(this, level, rate));
                    }

                    balance = 0;
                } else {
                    decreaseVl(0.0115);
                }
            }

            lastPlayerPacketTime = now;
        } else if (packet instanceof SPacketPosLook) {
            // We need to account for the extra flying packet the client sends when they confirm a teleport
            balance -= 50L;
        }
    }
}
