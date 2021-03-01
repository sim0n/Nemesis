package dev.sim0n.anticheat.check.impl.fly;

import dev.sim0n.anticheat.check.base.movement.PositionCheck;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementData;
import dev.sim0n.anticheat.util.data.CustomLocation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.PlayerViolation;
import org.bukkit.Bukkit;

/**
 * This checks for ground spoofing
 */
public class FlyC extends PositionCheck {
    public FlyC(PlayerData playerData) {
        super(playerData, "Fly C", new ViolationHandler(5, 30000L));
    }

    @Override
    public void handle(CustomLocation to, CustomLocation from, MovementData data) {
        if (!data.isOnGround() && to.isOnGround()) {
            if (++vl > 2.5) {
                vl = 2.5;

                handleViolation(new PlayerViolation(this));
            }
        } else {
            decreaseVl(0.225);
        }
    }
}
