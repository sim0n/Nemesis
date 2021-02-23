package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;

@Getter
public class SPacketEntityVelocity extends EPacket<PacketPlayOutEntityVelocity> {
    protected int entityId;

    protected short motionX, motionY, motionZ;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        motionX = serializer.readShort();
        motionY = serializer.readShort();
        motionZ = serializer.readShort();
    }
}
