package dev.sim0n.anticheat.check.impl.pingspoof;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

public class PingSpoofB extends Check {
    public PingSpoofB(PlayerData playerData) {
        super(playerData, "Ping Spoof B", new ViolationHandler(5, 60000L));
    }
}
