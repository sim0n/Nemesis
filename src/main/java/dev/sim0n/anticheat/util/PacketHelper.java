package dev.sim0n.anticheat.util;

import dev.sim0n.anticheat.net.packet.ClientBoundPacket;
import dev.sim0n.anticheat.net.packet.ServerBoundPacket;
import lombok.experimental.UtilityClass;

// This class provides a simple way to get ids from some specific packets
@UtilityClass
public class PacketHelper {

    public final int[] FLYING_IDS = new int[] {
            ServerBoundPacket.PLAY_PLAYER.getId(),
            ServerBoundPacket.PLAY_LOOK.getId(),
            ServerBoundPacket.PLAY_POSITION.getId(),
            ServerBoundPacket.PLAY_POSITION_LOOK.getId()
    };

    public final int[] ENTITY_IDS = new int[] {
            ClientBoundPacket.PLAY_ENTITY_REL_MOVE.getId(),
            ClientBoundPacket.PLAY_ENTITY_REL_MOVE_LOOK.getId(),
    };
}
