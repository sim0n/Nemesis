package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;

@Getter
public class CPacketCloseWindow extends EPacket<PacketPlayInCloseWindow> {
    protected int windowId;

    @Override
    public void handle(PacketDataSerializer serializer) {
        windowId = serializer.readByte();
    }
}
