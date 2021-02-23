package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class CPacketPosition extends CPacketPlayer {

    @Override
    public void handle(PacketDataSerializer serializer) {
        x = serializer.readDouble();
        y = serializer.readDouble();
        z = serializer.readDouble();

        moving = true;

        super.handle(serializer);
    }
}

