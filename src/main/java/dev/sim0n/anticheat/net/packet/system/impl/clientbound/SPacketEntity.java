package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;

@Getter
public class SPacketEntity extends EPacket<PacketPlayOutEntity> {
    protected int entityId;

    protected byte posX, posY, posZ;

    protected byte yaw, pitch;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();
    }
}
