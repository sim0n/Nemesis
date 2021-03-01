package dev.sim0n.anticheat.check.impl.autoclicker;

import dev.sim0n.anticheat.check.base.packet.swing.AirSwingDeltaCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.Queue;

public class AutoClickerC2 extends AirSwingDeltaCheck {
    public AutoClickerC2(PlayerData playerData) {
        super(playerData, "Auto Clicker C2", ViolationHandler.NO_BAN, 100);
    }

    @Override
    public void handle(Queue<Integer> samples) {
        double stDev = MathUtil.getStandardDeviation(samples);

        if (stDev < 0.45) {
            if (++vl > 2) {
                handleViolation(new DetailedPlayerViolation(this, stDev));
            }
        } else {
            decreaseVl(0.55);
        }
    }
}
