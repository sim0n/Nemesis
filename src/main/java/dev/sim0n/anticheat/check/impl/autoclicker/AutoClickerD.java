package dev.sim0n.anticheat.check.impl.autoclicker;

import dev.sim0n.anticheat.check.base.packet.swing.AirSwingCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.Queue;

public class AutoClickerD extends AirSwingCheck {
    public AutoClickerD(PlayerData playerData) {
        super(playerData, "Auto Clicker D", ViolationHandler.NO_BAN, 500);
    }

    @Override
    public void handle(Queue<Integer> samples) {
        double kurtosis = MathUtil.getKurtosis(samples);

        if (kurtosis < 0D) {
            if (++vl > 1) {
                handleViolation(new DetailedPlayerViolation(this, kurtosis));
            }
        } else {
            decreaseVl(0.5);
        }
    }
}
