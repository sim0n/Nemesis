package dev.sim0n.anticheat.net;

import dev.sim0n.anticheat.net.packet.handler.PacketHandler;
import dev.sim0n.anticheat.player.PlayerData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {
    // We shouldn't inject on the main thread...
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void injectPacketHandler(PlayerData playerData, Player player) {
        executor.submit(() -> {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

            Channel channel = playerConnection.networkManager.channel;
            ChannelPipeline pipeline = channel.pipeline();

            pipeline.addBefore("packet_handler", "ac-handler", new PacketHandler(playerData));
        });
    }

    public void ejectPacketHandler(Player player) {
        executor.submit(() -> {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

            if (playerConnection != null && !playerConnection.isDisconnected()) {
                Channel channel = playerConnection.networkManager.channel;
                ChannelPipeline pipeline = channel.pipeline();

                channel.eventLoop().execute(() -> {
                    if (pipeline.get("ac-handler") != null)
                        pipeline.remove("ac-handler");
                });
            }
        });
    }
}
