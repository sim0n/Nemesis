package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutRemoveEntityEffect;

@Getter
public class SPacketRemoveEntityEffect extends EPacket<PacketPlayOutRemoveEntityEffect> {
    protected int entityId;

    protected byte effectId;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        effectId = serializer.readByte();
    }
}
