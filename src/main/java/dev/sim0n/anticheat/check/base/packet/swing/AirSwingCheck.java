package dev.sim0n.anticheat.check.base.packet.swing;

import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketAnimation;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AirSwingCheck extends PacketCheck {
    private static final int MAX_COMBAT_TICKS = 20 * 60; // 1 minute

    private final Queue<Integer> samples = new LinkedList<>();

    private final boolean combatCheck; // if we should only check while in combat
    private final boolean doubleClicks; // if we should allow double clicks

    private final int maxSamples;

    private int movements;

    public AirSwingCheck(PlayerData playerData, String name, ViolationHandler violationHandler, int maxSamples) {
        super(playerData, name, violationHandler);

        this.maxSamples = maxSamples;

        combatCheck = false;
        doubleClicks = false;
    }

    public AirSwingCheck(PlayerData playerData, String name, ViolationHandler violationHandler, int maxSamples, boolean combatCheck) {
        super(playerData, name, violationHandler);

        this.maxSamples = maxSamples;
        this.combatCheck = combatCheck;

        doubleClicks = false;
    }

    public AirSwingCheck(PlayerData playerData, String name, ViolationHandler violationHandler, int maxSamples, boolean combatCheck, boolean doubleClicks) {
        super(playerData, name, violationHandler);

        this.maxSamples = maxSamples;
        this.combatCheck = combatCheck;
        this.doubleClicks = doubleClicks;
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (packet instanceof CPacketAnimation && !actionTracker.isDigging()) {
            if (movements < 10) {
                if (combatCheck && actionTracker.getLastAttack() > MAX_COMBAT_TICKS)
                    return;

                if (!doubleClicks && movements == 0)
                    return;

                if (samples.add(movements) && samples.size() == maxSamples) {
                    handle(samples);
                    samples.clear();
                }
            }

            movements = 0;
        } else if (packet instanceof CPacketPlayer) {
            ++movements;
        }
    }

    public abstract void handle(Queue<Integer> samples);
}
