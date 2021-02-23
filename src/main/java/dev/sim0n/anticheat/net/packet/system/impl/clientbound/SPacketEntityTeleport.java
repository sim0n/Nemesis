package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

@Getter
public class SPacketEntityTeleport extends EPacket<PacketPlayOutEntityTeleport> {
    protected int entityId;

    protected int posX, posY, posZ;

    protected byte yaw, pitch;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        posX = serializer.readInt();
        posY = serializer.readInt();
        posZ = serializer.readInt();

        yaw = serializer.readByte();
        pitch = serializer.readByte();
    }
}
