package dev.sim0n.anticheat.check.base.movement;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;

public abstract class PositionCheck extends Check {
    protected static final double JUMP_MOMENTUM = 0.42F;

    protected static final double WORLD_GRAVITY = 0.08;
    protected static final double VERTICAL_AIR_FRICTION = 0.98F;

    protected static final float JUMP_MOVEMENT_FACTOR = 0.026F;
    protected static final float LAND_MOVEMENT_FACTOR = 0.16277136F;

    protected static final float SPRINT_BOOST = 1.3F;

    protected static final float AIR_FRICTION = 0.91F;

    protected static final double JUMP_BOOST = 0.2;

    public PositionCheck(PlayerData playerData, String name, ViolationHandler violationHandler) {
        super(playerData, name, violationHandler);
    }

    public abstract void handle(CustomLocation to, CustomLocation from, MovementData data);
}
