package dev.sim0n.anticheat.check.impl.autoclicker;

import dev.sim0n.anticheat.check.base.packet.swing.AirSwingCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.Queue;

public class AutoClickerA extends AirSwingCheck {
    public AutoClickerA(PlayerData playerData) {
        super(playerData, "Auto Clicker A", new ViolationHandler(5, 60000L), 50, false, true);
    }

    @Override
    public void handle(Queue<Integer> samples) {
        double cps = MathUtil.getCps(samples);

        if (cps > 20) {
            handleViolation(new DetailedPlayerViolation(this, cps));
        }
    }
}
