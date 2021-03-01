package dev.sim0n.anticheat.check.impl.autoclicker;

import dev.sim0n.anticheat.check.base.packet.swing.AirSwingCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.Queue;

public class AutoClickerC extends AirSwingCheck {
    public AutoClickerC(PlayerData playerData) {
        super(playerData, "Auto Clicker C", new ViolationHandler(10, 60000L * 3), 100, true);
    }

    @Override
    public void handle(Queue<Integer> samples) {
        double stDev = MathUtil.getStandardDeviation(samples);

        if (stDev < 0.45) {
            if (++vl > 2) {
                handleViolation(new DetailedPlayerViolation(this, stDev));
            }
        } else {
            decreaseVl(0.5);
        }
    }
}
