package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;

@Getter
public class CPacketConfirmTransaction extends EPacket<PacketPlayInTransaction> {
    protected int windowId;

    protected short uid;

    protected boolean accepted;

    @Override
    public void handle(PacketDataSerializer serializer) {
        windowId = serializer.readByte();

        uid = serializer.readShort();

        accepted = serializer.readByte() != 0;
    }
}
