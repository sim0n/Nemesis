package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class CPacketRotation extends CPacketPlayer {

    @Override
    public void handle(PacketDataSerializer serializer) {
        yaw = serializer.readFloat();
        pitch = serializer.readFloat();

        rotating = true;

        super.handle(serializer);
    }
}
