package dev.sim0n.anticheat.task;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.util.NmsUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerTickTask extends BukkitRunnable {
    private final static long MAX_ALLOWED_TICK_TIME = 100L;

    private long lastTickTime;

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        lastTickTime = now;

        AntiCheat.getInstance().getPlayerDataManager().values().forEach(playerData -> {
            short transactionId = playerData.getNextTransactionId();

            playerData.getPingTracker().getExpectedTransactions().put(transactionId, now);

            NmsUtil.sendTransactionPacket(playerData.getBukkitPlayer(), transactionId);
        });

    }

    /**
     * @return If the server is lagging
     */
    public boolean isLagging() {
        return System.currentTimeMillis() - lastTickTime > MAX_ALLOWED_TICK_TIME;
    }
}
