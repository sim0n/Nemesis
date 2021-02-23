package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;

@Getter
public class CPacketKeepAlive extends EPacket<PacketPlayInKeepAlive> {
    protected int key;

    @Override
    public void handle(PacketDataSerializer serializer) {
        key = serializer.e();
    }
}
