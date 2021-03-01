package dev.sim0n.anticheat.check.impl.aimassist;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

public class AimAssistC extends RotationCheck {
    private int passes;

    private Float lastPitchChange;
    private Float lastPitchAccel;
    private Float lastPitchJolt;

    public AimAssistC(PlayerData playerData) {
        super(playerData, "Aim Assist C", ViolationHandler.NO_BAN);
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        float pitchChange = to.getPitch() - from.getPitch();

        if (lastPitchChange != null) {
            float pitchAccel = lastPitchChange - pitchChange;

            if (lastPitchAccel != null) {
                float pitchJolt = lastPitchAccel - pitchAccel;

                if (lastPitchJolt != null) {
                    if (lastPitchJolt * pitchJolt < 0F)
                        ++vl;
                    else
                        ++passes;

                    if (vl + passes == 200) {
                        if (vl == 0 || vl == 200)
                            handleViolation(new DetailedPlayerViolation(this, vl));

                        vl = passes = 0;
                    }
                }

                lastPitchJolt = pitchJolt;
            }

            lastPitchAccel = pitchAccel;
        }

        lastPitchChange = pitchChange;
    }
}
