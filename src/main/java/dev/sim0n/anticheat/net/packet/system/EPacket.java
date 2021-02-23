package dev.sim0n.anticheat.net.packet.system;

import lombok.Data;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

@Data
public abstract class EPacket<T extends Packet> {
    private long timestamp;
    private int id;

    // Handles a PacketDataSerializer to avoid the use of reflection
    public abstract void handle(PacketDataSerializer serializer);
}
