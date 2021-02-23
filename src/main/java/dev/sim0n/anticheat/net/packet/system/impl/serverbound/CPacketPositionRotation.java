package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class CPacketPositionRotation extends CPacketPlayer {

    @Override
    public void handle(PacketDataSerializer serializer) {
        x = serializer.readDouble();
        y = serializer.readDouble();
        z = serializer.readDouble();

        yaw = serializer.readFloat();
        pitch = serializer.readFloat();

        moving = true;
        rotating = true;

        super.handle(serializer);
    }
}


