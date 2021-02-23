package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.util.data.Direction;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.util.Vector;

@Getter
public class CPacketPlayerDigging extends EPacket<PacketPlayInBlockDig> {
    protected Vector blockPos;

    protected Direction direction;

    protected Action action;

    @Override
    public void handle(PacketDataSerializer serializer) {
        action = Action.values()[serializer.a(PacketPlayInBlockDig.EnumPlayerDigType.class).ordinal()];

        BlockPosition blockPosition = serializer.c();

        blockPos = new Vector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());

        direction = Direction.values()[EnumDirection.fromType1(serializer.readUnsignedByte()).ordinal()];
    }

    public enum Action {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM
    }
}
