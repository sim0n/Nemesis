package dev.sim0n.anticheat.check;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.check.base.movement.RotationCheck;
import dev.sim0n.anticheat.check.base.packet.PacketCheck;
import dev.sim0n.anticheat.player.PlayerData;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CheckData {
    private Set<Check> checks;
    private Set<PacketCheck> packetChecks;
    private Set<PositionCheck> positionChecks;
    private Set<RotationCheck> rotationChecks;

    public void enable(PlayerData playerData) {
        CheckManager checkManager = AntiCheat.getInstance().getCheckManager();

        checks = checkManager.getConstructors().stream()
                .map(clazz -> {
                    try {
                        return (Check) clazz.newInstance(playerData);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        packetChecks = checks.stream()
                .filter(PacketCheck.class::isInstance)
                .map(PacketCheck.class::cast)
                .collect(Collectors.toSet());

        positionChecks = checks.stream()
                .filter(PositionCheck.class::isInstance)
                .map(PositionCheck.class::cast)
                .collect(Collectors.toSet());

        rotationChecks = checks.stream()
                .filter(RotationCheck.class::isInstance)
                .map(RotationCheck.class::cast)
                .collect(Collectors.toSet());
    }

    public <T> T getCheck(Class<T> clazz) {
        return (T) checks.stream()
                .filter(check -> check.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

}
