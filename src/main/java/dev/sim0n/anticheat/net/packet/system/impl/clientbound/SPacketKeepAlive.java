package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;

@Getter
public class SPacketKeepAlive extends EPacket<PacketPlayOutKeepAlive> {
    protected int key;

    @Override
    public void handle(PacketDataSerializer serializer) {
        key = serializer.e();
    }
}
