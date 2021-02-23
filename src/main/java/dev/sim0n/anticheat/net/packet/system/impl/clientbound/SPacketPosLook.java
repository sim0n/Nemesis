package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;

@Getter
public class SPacketPosLook extends EPacket<PacketPlayOutPosition> {
    protected double x, y, z;

    protected float yaw, pitch;

    @Override
    public void handle(PacketDataSerializer serializer) {
        x = serializer.readDouble();
        y = serializer.readDouble();
        z = serializer.readDouble();

        yaw = serializer.readFloat();
        pitch = serializer.readFloat();
    }
}
