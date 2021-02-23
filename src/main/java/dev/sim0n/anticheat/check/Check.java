package dev.sim0n.anticheat.check;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.ActionTracker;
import dev.sim0n.anticheat.player.tracker.impl.StatusTracker;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementTracker;
import dev.sim0n.anticheat.player.tracker.impl.PingTracker;
import dev.sim0n.anticheat.violation.ViolationManager;
import dev.sim0n.anticheat.violation.base.AbstractPlayerViolation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import lombok.Getter;
import org.atteo.classindex.IndexSubclasses;

@Getter
@IndexSubclasses
public abstract class Check {
    protected final AntiCheat plugin = AntiCheat.getInstance();

    protected final ViolationManager violationManager = plugin.getViolationManager();

    protected final MovementTracker movementTracker;
    protected final ActionTracker actionTracker;
    protected final StatusTracker statusTracker;
    protected final PingTracker pingTracker;

    protected final PlayerData playerData;
    private final String name;

    private final ViolationHandler violationHandler;

    protected double vl;

    protected Check(PlayerData playerData, String name, ViolationHandler violationHandler) {
        this.playerData = playerData;
        this.name = name;
        this.violationHandler = violationHandler;

        movementTracker = playerData.getMovementTracker();
        actionTracker = playerData.getActionTracker();
        statusTracker = playerData.getStatusTracker();
        pingTracker = playerData.getPingTracker();
    }

    public void handleViolation(AbstractPlayerViolation violation) {
        violationManager.handleViolation(violation);
    }

    protected void decreaseVl(double decrease) {
        vl = Math.max(0, vl - decrease);
    }
}
