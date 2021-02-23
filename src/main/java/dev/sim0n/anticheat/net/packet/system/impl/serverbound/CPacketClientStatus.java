package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;

@Getter
public class CPacketClientStatus extends EPacket<PacketPlayInClientCommand> {
    protected State status;

    @Override
    public void handle(PacketDataSerializer serializer) {
        status = State.values()[serializer.a(PacketPlayInClientCommand.EnumClientCommand.class).ordinal()];
    }

    public enum State {
        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT;
    }
}
