package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

@Getter
public class CPacketPlayer extends EPacket<PacketPlayInFlying> {
    protected double x, y, z;

    protected float yaw, pitch;

    protected boolean onGround, moving, rotating;

    @Override
    public void handle(PacketDataSerializer serializer) {
        onGround = serializer.readUnsignedByte() != 0;
    }
}
