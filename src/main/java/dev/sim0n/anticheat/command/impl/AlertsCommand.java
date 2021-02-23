package dev.sim0n.anticheat.command.impl;

import dev.sim0n.anticheat.command.BaseCommand;
import dev.sim0n.anticheat.command.CommandManifest;
import dev.sim0n.anticheat.util.CC;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@CommandManifest(permission = "nemesis.mod.alerts")
public class AlertsCommand extends BaseCommand {
    public AlertsCommand() {
        super("alerts");
    }

    @Override
    public void handle(Player player, List<String> args) {
        Set<UUID> alertsEnabled = plugin.getViolationManager().getAlertsEnabled();

        UUID uuid = player.getUniqueId();

        if (!alertsEnabled.remove(uuid))
            alertsEnabled.add(uuid);

        boolean hasAlerts = alertsEnabled.contains(uuid);

        sendMessage(player, String.format("%sYou%s have toggled alerts %s%s.",
                CC.YELLOW, CC.GRAY, hasAlerts ? CC.GREEN + "ON" : CC.RED + "OFF", CC.GRAY));
    }
}
