package dev.sim0n.anticheat.task;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.database.DataBaseManager;
import dev.sim0n.anticheat.log.Log;
import dev.sim0n.anticheat.log.LogManager;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class ExportLogsTask extends BukkitRunnable {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private final DataBaseManager dbManager = plugin.getDataBaseManager();
    private final LogManager logManager = plugin.getLogManager();

    @Override
    public void run() {
        Queue<Log> queuedLogs = logManager.getQueuedLogs();

        if (queuedLogs.isEmpty())
            return;

        dbManager.getLogsCollection().insertMany(queuedLogs.stream()
                .map(Log::toDocument)
                .collect(Collectors.toList()));

        queuedLogs.clear();
    }
}
