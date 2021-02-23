package dev.sim0n.anticheat.player.tracker.impl.movement;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovementData {
    private boolean onGround, wasOnGround;
    private boolean underBlock, wasUnderBlock;
    private boolean onLadder, wasOnLadder;
    private boolean inLiquid, wasInLiquid;

    private boolean collidedHorizontally, wasCollidedHorizontally;
    private boolean collidedVertically, wasCollidedVertically;

    /**
     * Handles state updates for movement
     * @param colliding If colliding with a block horizontally
     * @param onGround If colliding with a block below
     * @param underBlock If colliding with a block above
     */
    public void handle(boolean colliding, boolean onGround, boolean underBlock, boolean onLadder, boolean inLiquid) {
        wasCollidedHorizontally = this.collidedHorizontally;
        collidedHorizontally = colliding;

        wasOnGround = this.onGround;
        this.onGround = onGround;

        wasUnderBlock = this.underBlock;
        this.underBlock = underBlock;

        wasOnLadder = this.onLadder;
        this.onLadder = onLadder;

        wasInLiquid = this.inLiquid;
        this.inLiquid = inLiquid;

        wasCollidedVertically = collidedVertically;
        collidedVertically = onGround || underBlock;
    }
}
