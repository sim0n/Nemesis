package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;

@Getter
public class CPacketHeldItemChange extends EPacket<PacketPlayInHeldItemSlot> {
    protected int slotId;

    @Override
    public void handle(PacketDataSerializer serializer) {
        slotId = serializer.readShort();
    }
}
