package dev.sim0n.anticheat.player;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.check.CheckData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.player.tracker.impl.ActionTracker;
import dev.sim0n.anticheat.player.tracker.impl.entity.EntityTracker;
import dev.sim0n.anticheat.player.tracker.impl.movement.MovementTracker;
import dev.sim0n.anticheat.player.tracker.impl.PingTracker;
import dev.sim0n.anticheat.player.tracker.impl.StatusTracker;
import dev.sim0n.anticheat.util.NmsUtil;
import dev.sim0n.anticheat.violation.log.ViolationLog;
import lombok.Data;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

@Data
public class PlayerData {
    private static final Map<Field, Constructor<?>> CONSTRUCTORS = new HashMap<>();

    static {
        Arrays.stream(PlayerData.class.getDeclaredFields())
                .filter(field -> PlayerTracker.class.isAssignableFrom(field.getType()))
                .forEach(field -> {
                    Class<? extends PlayerTracker> clazz = (Class<? extends PlayerTracker>) field.getType();

                    try {
                        CONSTRUCTORS.put(field, clazz.getConstructor(PlayerData.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    private final Map<Check, Set<ViolationLog>> violations = new HashMap<>();
    private final Set<PlayerTracker> trackers = new HashSet<>();
    private final CheckData checkData = new CheckData();

    private final Player bukkitPlayer;

    private int entityId;

    private MovementTracker movementTracker;
    private EntityTracker entityTracker;
    private ActionTracker actionTracker;
    private StatusTracker statusTracker;
    private PingTracker pingTracker;

    private boolean banning;

    private int protocolVersion = 47;

    private short transactionId = Short.MIN_VALUE;

    private int ticksExisted;

    public PlayerData(Player player) {
        bukkitPlayer = player;

        entityId = bukkitPlayer.getEntityId();

        CONSTRUCTORS.forEach((field, constructor) -> {
            try {
                PlayerTracker tracker = (PlayerTracker) constructor.newInstance(this);

                trackers.add(tracker);

                field.set(this, tracker);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        checkData.enable(this);
    }

    /**
     * Adds a logged violation to {@param check}
     * @param check The check to add the logged violation to
     * @param log The violation log
     */
    public void addViolation(Check check, ViolationLog log) {
        violations.computeIfAbsent(check, c -> new HashSet<>())
                .add(log);
    }

    /**
     * Gets all violation level for {@param check}
     * @param check The check to get the violation level for
     * @return The violation level
     */
    public int getViolationLevel(Check check) {
        return violations.computeIfAbsent(check, c -> new HashSet<>())
                .stream()
                .mapToInt(ViolationLog::getLevel)
                .sum();
    }

    /**
     * Gets all violation level within a certain time length
     * @param check The check to get the violation level for
     * @param length The time length to collect the violations from
     * @return The violation level
     */
    public int getViolationLevel(Check check, long length) {
        long now = System.currentTimeMillis();

        return violations.computeIfAbsent(check, c -> new HashSet<>())
                .stream()
                .filter(violation -> now + length > violation.getTimestamp())
                .mapToInt(ViolationLog::getLevel)
                .sum();
    }

    /**
     * Sends a transaction with {@param callback} bound to it which will be called upon the client
     * sending the same transaction uid back
     * @param callback The callback
     */
    public void sendTransaction(Consumer<Short> callback) {
        short id = getNextTransactionId();

        pingTracker.getScheduledTransactions().put(id, callback);
        NmsUtil.sendTransactionPacket(bukkitPlayer, id);
    }

    public short getNextTransactionId() {
        ++transactionId;

        if (transactionId == -1)
            transactionId = Short.MIN_VALUE;

        return transactionId;
    }
}
