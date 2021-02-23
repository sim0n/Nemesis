package dev.sim0n.anticheat.command.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import dev.sim0n.anticheat.command.BaseCommand;
import dev.sim0n.anticheat.command.CommandManifest;
import dev.sim0n.anticheat.log.Log;
import dev.sim0n.anticheat.util.CC;
import dev.sim0n.anticheat.util.HttpUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@CommandManifest(permission = "nemesis.mod.logs", async = true)
public class LogsCommand extends BaseCommand {
    public LogsCommand() {
        super("logs");
    }

    @Override
    public void handle(Player player, List<String> args) {
        if (args.size() < 1)
            return;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.get(0));

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            sendMessage(player, CC.RED + "Player doesn't exist or has never played.");
            return;
        }

        long now = System.currentTimeMillis();

        UUID uuid = offlinePlayer.getUniqueId();

        Iterable<Log> logs = plugin.getDataBaseManager().getLogsCollection()
                .find(Filters.eq("uuid", uuid.toString()))
                .sort(Indexes.descending("_id"))
                .map(Log::fromDocument);

        StringBuilder sb = new StringBuilder();

        // Since the queued logs may not be pushed to the database yet we will have to check them too
        plugin.getLogManager().getQueuedLogs().stream()
                .filter(log -> log.getUuid().equals(uuid))
                .forEach(log -> appendLog(sb, log, now));

        logs.forEach(log -> appendLog(sb, log, now));

        if (sb.length() == 0) {
            sendMessage(player, CC.RED + "No logs found for " + offlinePlayer.getName());
            return;
        }

        String key = HttpUtil.getPastie(sb.toString());

        sendMessage(player, String.format("Uploaded log to %shttps://pastie.io/%s", CC.GRAY, key));
    }

    private void appendLog(StringBuilder sb, Log log, long now) {
        sb.append("[")
                .append(DurationFormatUtils.formatDuration(now - log.getTimestamp(), "dd:HH:mm:ss"))
                .append("] ");
        sb.append(log.getCheck()).append(" VL ");
        sb.append(log.getVl()).append(" ");
        sb.append(log.getData());
        sb.append("\n");
    }
}
