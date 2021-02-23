package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;

@Getter
public class SPacketConfirmTransaction extends EPacket<PacketPlayOutTransaction> {
    protected int windowId;

    protected short uid;

    protected boolean accepted;

    @Override
    public void handle(PacketDataSerializer serializer) {
        windowId = serializer.readUnsignedByte();

        uid = serializer.readShort();

        accepted = serializer.readBoolean();
    }
}
