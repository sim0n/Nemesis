package dev.sim0n.anticheat.check.base.packet;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.PlayerViolation;

import java.util.function.Predicate;

/**
 * This check detect clients performing actions at the end of the tick (aka post motion check)
 */
public abstract class PostActionCheck extends PacketCheck {
    private final Predicate<EPacket<?>> condition;

    private boolean sent;

    public PostActionCheck(PlayerData playerData, String name, Predicate<EPacket<?>> condition) {
        super(playerData, name, new ViolationHandler(10, 30000L));

        this.condition = condition;
    }

    @Override
    public void handle(EPacket<?> packet) {
        if (movementTracker.isTeleporting()) {
            sent = false;
            return;
        }

        if (condition.test(packet)) {
            long delay = packet.getTimestamp() - movementTracker.getLastLocation().getTimestamp();

            if (delay < 2L) {
                sent = true;
            } else {
                decreaseVl(0.2);
            }
        } else if (packet instanceof CPacketPlayer && sent) {
            long delay = movementTracker.getLastLocation().getTimestamp() - movementTracker.getLastLastLocation().getTimestamp();

            if (delay > 40L && delay < 60L) {
                if (++vl > 3) {
                    vl = 3;

                    handleViolation(new PlayerViolation(this));
                }
            } else {
                decreaseVl(0.2);
            }

            sent = false;
        }
    }
}
