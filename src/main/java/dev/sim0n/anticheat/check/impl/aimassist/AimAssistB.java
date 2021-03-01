package dev.sim0n.anticheat.check.impl.aimassist;

import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;

import java.util.Deque;
import java.util.LinkedList;

public class AimAssistB extends RotationCheck {
    private final Deque<Float> pitchChanges = new LinkedList<>();

    private Float lastPitchChange;

    public AimAssistB(PlayerData playerData) {
        super(playerData, "Aim Assist B", ViolationHandler.NO_BAN);
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from) {
        if (actionTracker.getLastAttack() > 40) {
            pitchChanges.clear();
            return;
        }

        if (Math.abs(to.getYaw() - from.getYaw()) < 1F)
            return;

        float pitchChange = Math.abs(to.getPitch() - from.getPitch());

        if (lastPitchChange != null) {
            if (pitchChanges.add(pitchChange) && pitchChanges.size() == 30) {
                pitchChanges.removeFirst();

                double min = pitchChanges.stream()
                        .mapToDouble(Float::doubleValue)
                        .min()
                        .orElse(0);

                double max = pitchChanges.stream()
                        .mapToDouble(Float::doubleValue)
                        .max()
                        .orElse(0);

                if (Math.abs(max - min) < 0.1F) {
                    handleViolation(new DetailedPlayerViolation(this, Math.abs(max - min)));
                }
            }
        }

        lastPitchChange = pitchChange;
    }
}
