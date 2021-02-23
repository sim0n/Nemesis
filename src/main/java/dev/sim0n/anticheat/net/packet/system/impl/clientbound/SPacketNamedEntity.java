package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

@Getter
public class SPacketNamedEntity extends EPacket<PacketPlayOutNamedEntitySpawn> {
    protected int entityId;

    protected int posX, posY, posZ;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        posX = serializer.readInt();
        posY = serializer.readInt();
        posZ = serializer.readInt();
    }
}
