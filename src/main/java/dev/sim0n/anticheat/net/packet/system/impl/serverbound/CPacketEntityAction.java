package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;

@Getter
public class CPacketEntityAction extends EPacket<PacketPlayInEntityAction> {
    protected Action action;

    @Override
    public void handle(PacketDataSerializer serializer) {
        // animation
        serializer.e();

        action = Action.values()[serializer.a(PacketPlayInEntityAction.EnumPlayerAction.class).ordinal()];
    }

    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        RIDING_JUMP,
        OPEN_INVENTORY
    }
}
