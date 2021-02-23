package dev.sim0n.anticheat.net.packet.system.impl.clientbound;

import com.google.common.collect.Lists;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class SPacketUpdateAttributes extends EPacket<PacketPlayOutUpdateAttributes> {
    protected int entityId;

    private final List<Snapshot> snapshots = new ArrayList<>();

    @Override
    public void handle(PacketDataSerializer serializer) {
        entityId = serializer.e();

        int i = serializer.readInt();

        for(int j = 0; j < i; ++j) {
            String name = serializer.c(64);
            double baseValue = serializer.readDouble();
            int k = serializer.e();

            for(int l = 0; l < k; ++l) {
                UUID var10 = serializer.g();

                serializer.readDouble();
                serializer.readByte();
            }

            snapshots.add(new Snapshot(name, baseValue));
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Snapshot {
        private final String name;

        private final double baseValue;
    }
}
