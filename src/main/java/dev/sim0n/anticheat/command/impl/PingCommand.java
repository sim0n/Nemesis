package dev.sim0n.anticheat.command.impl;

import dev.sim0n.anticheat.command.BaseCommand;
import dev.sim0n.anticheat.command.CommandManifest;
import dev.sim0n.anticheat.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@CommandManifest(permission = "nemesis.ping")
public class PingCommand extends BaseCommand {
    public PingCommand() {
        super("ping");
    }

    @Override
    public void handle(Player player, List<String> args) {
        if (args.size() > 0) {
            Player target = Bukkit.getPlayer(args.get(0));

            if (target == null) {
                player.sendMessage(String.format("%sUnable to find %s, are they online?", CC.RED, args.get(0)));
                return;
            }

            sendMessage(player, String.format("%s%s%s's ping is %sms",
                    CC.YELLOW, target.getName(), CC.GRAY, plugin.getPlayerDataManager().getData(target).getPingTracker().getTransactionPing()));
        } else {
            sendMessage(player, String.format("%sYour%s ping is %sms",
                    CC.YELLOW, CC.GRAY, plugin.getPlayerDataManager().getData(player).getPingTracker().getTransactionPing()));
        }
    }
}
