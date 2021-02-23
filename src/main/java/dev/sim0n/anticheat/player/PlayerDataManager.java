package dev.sim0n.anticheat.player;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public void registerData(Player player) {
        dataMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData getData(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    public PlayerData removeData(Player player) {
        return dataMap.remove(player.getUniqueId());
    }

    public Collection<PlayerData> values() {
        return dataMap.values();
    }
}
