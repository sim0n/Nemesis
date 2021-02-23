package dev.sim0n.anticheat.violation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.log.Log;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.violation.base.AbstractPlayerViolation;
import dev.sim0n.anticheat.violation.handler.ViolationHandler;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import dev.sim0n.anticheat.violation.log.ViolationLog;
import dev.sim0n.anticheat.violation.punishment.PlayerBan;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViolationManager {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private static final String VIOLATION_ALERT_FORMAT = AntiCheat.PREFIX + "&e%s &7failed &e%s. &7VL %d. %s";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder()
                    .setPriority(3)
                    .setNameFormat("Nemesis Violation Executor Thread")
                    .build()
    );

    @Getter
    private final Set<UUID> alertsEnabled = new HashSet<>();

    public void handleViolation(AbstractPlayerViolation violation) {
        executorService.submit(() -> {
            Check check = violation.getCheck();
            PlayerData playerData = check.getPlayerData();

            if (playerData.isBanning())
                return;

            playerData.addViolation(check, new ViolationLog(violation.getPoints()));

            int violations = playerData.getViolationLevel(check);

            String playerName = playerData.getBukkitPlayer().getName();
            String checkName = check.getName();

            ViolationHandler handler = check.getViolationHandler();

            boolean experimental = handler == ViolationHandler.NO_BAN;

            if (experimental)
                checkName = "*" + checkName;

            String data = violation instanceof DetailedPlayerViolation ? violation.getData() : "";

            String alert = ChatColor.translateAlternateColorCodes('&', String.format(VIOLATION_ALERT_FORMAT, playerName, checkName, violations, data));

            int violationPoints = playerData.getViolationLevel(check, handler.getMaxViolationTimeLength());

            if (!experimental && violationPoints > handler.getMaxViolations()) {
                if (plugin.isLagging())
                    return;

                handleBan(new PlayerBan(playerData, String.format("%s VL %d", checkName, violations)));
            }

            alertsEnabled.stream()
                    .map(Bukkit::getPlayer)
                    .forEach(player -> player.sendMessage(alert));

            if (!plugin.getConfiguration().isDatabaseEnabled())
                return;

            Log log = new Log(playerData.getBukkitPlayer().getUniqueId(), checkName, data, violationPoints);

            plugin.getLogManager().getQueuedLogs().add(log);
        });
    }

    public void handleBan(PlayerBan ban) {
        PlayerData playerData = ban.getPlayerData();

        if (playerData.isBanning())
            return;

        playerData.setBanning(true);

        String playerName = playerData.getBukkitPlayer().getName();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("kick %s %s", playerName, ban.getReason()));
        });

    }


}
