package dev.sim0n.anticheat.check.base.packet.swing;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketAnimation;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AirSwingDeltaCheck extends PacketCheck {
    private static final int MAX_COMBAT_TICKS = 20 * 60; // 1 minute

    private final Queue<Integer> samples = new LinkedList<>();

    private final boolean combatCheck; // if we should only check while in combat

    private final int maxSamples;

    private Integer lastMovements;
    private int movements;

    public AirSwingDeltaCheck(PlayerData playerData, String name, ViolationHandler violationHandler, int maxSamples) {
        super(playerData, name, violationHandler);

        this.maxSamples = maxSamples;

        combatCheck = false;
    }

    public AirSwingDeltaCheck(PlayerData playerData, String name, ViolationHandler violationHandler, int maxSamples, boolean combatCheck) {
        super(playerData, name, violationHandler);

        this.maxSamples = maxSamples;
        this.combatCheck = combatCheck;
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketAnimation && !actionTracker.isDigging()) {
            if (lastMovements != null) {
                if (movements < 10) {
                    if (combatCheck && actionTracker.getLastAttack() > MAX_COMBAT_TICKS)
                        return;

                    if (samples.add(lastMovements - movements) && samples.size() == maxSamples) {
                        handle(samples);
                        samples.clear();
                    }
                }
            }

            lastMovements = movements;
            movements = 0;
        } else if (packet instanceof CPacketPlayer) {
            ++movements;
        }
    }

    public abstract void handle(Queue<Integer> samples);
}
