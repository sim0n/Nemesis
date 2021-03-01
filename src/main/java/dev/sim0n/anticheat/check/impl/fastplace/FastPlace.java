package dev.sim0n.anticheat.check.impl.fastplace;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketBlockPlacement;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.LinkedList;
import java.util.Queue;

public class FastPlace extends PacketCheck {
    private final Queue<Integer> delays = new LinkedList<>();
    
    private int movements;

    public FastPlace(PlayerData playerData) {
        super(playerData, "Fast Place", ViolationHandler.NO_BAN);
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketBlockPlacement) {
            if (movements < 10) {
                if (delays.add(movements) && delays.size() == 35) {
                    double avg = MathUtil.getAverage(delays);
                    double stDev = MathUtil.getStandardDeviation(delays);

                    if (avg < 4 && stDev < 0.15) {
                        String data = String.format("AVG %s SD %s", avg, stDev);

                        handleViolation(new DetailedPlayerViolation(this, data));
                    }

                    delays.clear();
                }
            }

            movements = 0;
        } else if (packet instanceof CPacketPlayer) {
            ++movements;
        }
    }
}
