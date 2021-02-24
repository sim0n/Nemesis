package dev.sim0n.anticheat.net.packet.handler;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketHeldItemChange;
import dev.sim0n.anticheat.player.PlayerData;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PacketHandler extends ChannelDuplexHandler {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private final PlayerData playerData;

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        long now = System.currentTimeMillis();

        Packet<PacketListenerPlayIn> nmsPacket = (Packet<PacketListenerPlayIn>) o;

        EPacket<?> packet = plugin.getPacketManager().handle(nmsPacket, true, now);

        try {
            handlePacket(packet, true, true);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        super.channelRead(channelHandlerContext, o);

        try {
            playerData.getCheckData().getPacketChecks().forEach(check -> check.handle(packet));

            handlePacket(packet, false, true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        long now = System.currentTimeMillis();

        Packet<PacketListenerPlayOut> nmsPacket = (Packet<PacketListenerPlayOut>) o;

        EPacket<?> packet = plugin.getPacketManager().handle(nmsPacket, false, now);

        try {
            handlePacket(packet, true, false);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        super.write(channelHandlerContext, o, channelPromise);

        try {
            playerData.getCheckData().getPacketChecks().forEach(check -> check.handle(packet));

            handlePacket(packet, false, false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void handlePacket(EPacket<?> packet, boolean pre, boolean incoming) {
        if (packet == null)
            return;

        int packetId = packet.getId();

        playerData.getTrackers().stream()
                .map(tracker -> incoming ?
                        (pre ? tracker.getPrePacketHandlers() : tracker.getPostPacketHandlers()).get(packetId) :
                        (pre ? tracker.getOutgoingPrePacketHandlers() : tracker.getOutgoingPostPacketHandlers()).get(packetId))
                .filter(Objects::nonNull)
                .forEach(handler -> handler.accept(packet));
    }
}
