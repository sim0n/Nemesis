package dev.sim0n.anticheat.check.impl.aimassist;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.MathUtil;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

/**
 * This checks if your pitch <a href="https://en.wikipedia.org/wiki/Greatest_common_divisor">gcd</a> is lower than the lowest possible on yawn.
 * There are some minor measures in place to prevent optifine zooming from spamming the chat, however it is not perfect
 */
public class AimAssistD extends RotationCheck {
    private Float lastYawChange;
    private Float lastPitchChange;

    public AimAssistD(PlayerData playerData) {
        super(playerData, "Aim Assist D", ViolationHandler.NO_BAN);
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        if (actionTracker.getLastAttack() > 10)
            return;

        float yawChange = Math.abs(to.getYaw() - from.getYaw());
        float pitchChange = Math.abs(to.getPitch() - from.getPitch());

        if (lastYawChange != null && lastPitchChange != null && to.distanceSquared(from) > 0) {
            float yawAccel = Math.abs(lastYawChange - yawChange);
            float pitchAccel = Math.abs(lastPitchChange - pitchChange);

            if (yawChange > 3F && pitchChange < 10F && yawAccel > 2F && pitchAccel > 2F && pitchChange < yawChange) {
                double pitchGcd = MathUtil.gcd(pitchChange, lastPitchChange);

                if (pitchGcd < 0.009) {
                    if (++vl > 1) {
                        handleViolation(new DetailedPlayerViolation(this, pitchGcd));
                    }
                } else {
                    decreaseVl(0.005);
                }
            } else {
                decreaseVl(0.3);
            }
        }

        this.lastYawChange = yawChange;
        this.lastPitchChange = pitchChange;
    }
}
