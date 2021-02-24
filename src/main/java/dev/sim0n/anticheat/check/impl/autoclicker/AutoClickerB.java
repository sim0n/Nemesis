package dev.sim0n.anticheat.check.impl.autoclicker;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketUseEntity;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.LinkedList;
import java.util.Queue;

public class AutoClickerB extends PacketCheck {
    private final Queue<Integer> delays = new LinkedList<>();

    private int movements;

    public AutoClickerB(PlayerData playerData) {
        super(playerData, "Auto Clicker B", new ViolationHandler(10, 45000L));
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketUseEntity && ((CPacketUseEntity) packet).getAction() == CPacketUseEntity.EnumEntityUseAction.ATTACK) {
            if (movements < 8) {
                delays.add(movements);

                if (delays.size() == 40) {
                    double stDev = MathUtil.getStandardDeviation(delays);

                    if (stDev < 0.45) {
                        int level = (int) (Math.ceil(1.1D - stDev) * 1.5);

                        if (++vl > 1) {
                            handleViolation(new DetailedPlayerViolation(this, level, stDev));
                        }
                    } else {
                        decreaseVl(0.5);
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
