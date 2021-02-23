package dev.sim0n.anticheat.check.impl.pingspoof;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

public class PingSpoofB2 extends Check {
    public PingSpoofB2(PlayerData playerData) {
        super(playerData, "Ping Spoof B2", new ViolationHandler(3, 60000L));
    }
}
