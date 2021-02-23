package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

@Getter
public class SPacketDestroyEntities extends EPacket<PacketPlayOutEntityDestroy> {
    protected int[] ids;

    @Override
    public void handle(PacketDataSerializer serializer) {
        ids = new int[serializer.e()];

        for (int i = 0; i < ids.length; ++i) {
            ids[i] = serializer.e();
        }
    }
}
