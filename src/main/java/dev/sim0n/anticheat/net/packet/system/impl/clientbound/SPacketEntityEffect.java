package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;

@Getter
public class SPacketEntityEffect extends EPacket<PacketPlayOutEntityEffect> {
    protected int entityId;

    protected byte effectId, amplifier;

    protected int duration;

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        effectId = serializer.readByte();
        amplifier = serializer.readByte();

        duration = serializer.e();
    }
}
