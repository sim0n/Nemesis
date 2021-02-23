package dev.sim0n.anticheat.listener;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private final PlayerDataManager playerDataManager = plugin.getPlayerDataManager();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        playerDataManager.registerData(player);

        plugin.getNetworkManager().injectPacketHandler(playerDataManager.getData(player), player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        playerDataManager.removeData(player);

        plugin.getNetworkManager().ejectPacketHandler(player);
    }
}
