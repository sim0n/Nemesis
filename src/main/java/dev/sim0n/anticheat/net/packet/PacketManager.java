package dev.sim0n.anticheat.net.packet;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.*;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.*;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EnumProtocol;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class PacketManager {
    // enum protocols * 256
    private static final int ENUM_PROTOCOL_LENGTH = 4 * 256;

    private final Class<? extends EPacket<?>>[] serverBoundPackets = new Class[ENUM_PROTOCOL_LENGTH];
    private final Class<? extends EPacket<?>>[] clientBoundPackets = new Class[ENUM_PROTOCOL_LENGTH];

    public PacketManager() {
        registerServerBound(ServerBoundPacket.PLAY_KEEP_ALIVE, CPacketKeepAlive.class);

        registerServerBound(ServerBoundPacket.PLAY_USE_ENTITY, CPacketUseEntity.class);

        registerServerBound(ServerBoundPacket.PLAY_PLAYER, CPacketPlayer.class);
        registerServerBound(ServerBoundPacket.PLAY_POSITION, CPacketPosition.class);
        registerServerBound(ServerBoundPacket.PLAY_LOOK, CPacketRotation.class);
        registerServerBound(ServerBoundPacket.PLAY_POSITION_LOOK, CPacketPositionRotation.class);

        registerServerBound(ServerBoundPacket.PLAY_BLOCK_DIG, CPacketPlayerDigging.class);
        registerServerBound(ServerBoundPacket.PLAY_BLOCK_PLACE, CPacketBlockPlacement.class);

        registerServerBound(ServerBoundPacket.PLAY_HELD_SLOT, CPacketHeldItemChange.class);
        registerServerBound(ServerBoundPacket.PLAY_ANIMATION, CPacketAnimation.class);

        registerServerBound(ServerBoundPacket.PLAY_WINDOW_CLOSE, CPacketCloseWindow.class);

        registerServerBound(ServerBoundPacket.PLAY_CLIENT_COMMAND, CPacketClientStatus.class);

        registerServerBound(ServerBoundPacket.PLAY_WINDOW_TRANSACTION, CPacketConfirmTransaction.class);

        registerServerBound(ServerBoundPacket.PLAY_ENTITY_ACTION, CPacketEntityAction.class);

        registerServerBound(ServerBoundPacket.PLAY_WINDOW_CLICK, CPacketClickWindow.class);

        // ======================================================================================================== //

        registerClientBound(ClientBoundPacket.PLAY_KEEP_ALIVE, SPacketKeepAlive.class);

        registerClientBound(ClientBoundPacket.PLAY_POSITION, SPacketPosLook.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY_VELOCITY, SPacketEntityVelocity.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY_TELEPORT, SPacketEntityTeleport.class);

        registerClientBound(ClientBoundPacket.PLAY_SPAWN_NAMED, SPacketNamedEntity.class);

        registerClientBound(ClientBoundPacket.PLAY_WINDOW_TRANSACTION, SPacketConfirmTransaction.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY_ATTRIBUTES, SPacketUpdateAttributes.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY_EFFECT_ADD, SPacketEntityEffect.class);
        registerClientBound(ClientBoundPacket.PLAY_ENTITY_EFFECT_REMOVE, SPacketRemoveEntityEffect.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY, SPacketEntity.class);
        registerClientBound(ClientBoundPacket.PLAY_ENTITY_LOOK, SPacketEntityLook.class);
        registerClientBound(ClientBoundPacket.PLAY_ENTITY_REL_MOVE, SPacketEntityRelMove.class);
        registerClientBound(ClientBoundPacket.PLAY_ENTITY_REL_MOVE_LOOK, SPacketEntityLookMove.class);

        registerClientBound(ClientBoundPacket.PLAY_ENTITY_DESTROY, SPacketDestroyEntities.class);
    }

    private void registerServerBound(ServerBoundPacket packet, Class<? extends EPacket<?>> packetClass) {
        serverBoundPackets[packet.getId()] = packetClass;
    }

    private void registerClientBound(ClientBoundPacket packet, Class<? extends EPacket<?>> packetClass) {
        clientBoundPackets[packet.getId()] = packetClass;
    }

    /**
     * Handles a packet conversion from {@param nmsPacket} to a {@link EPacket}
     * @param nmsPacket The nms packet to convert into an SPacket
     * @param inbound Whether it's inbound or not
     * @return A {@link EPacket}
     */
    @SneakyThrows
    public EPacket<?> handle(Packet<?> nmsPacket, boolean inbound, long now) {
        EnumProtocolDirection direction = inbound ? EnumProtocolDirection.SERVERBOUND : EnumProtocolDirection.CLIENTBOUND;

        // EnumProtocol#a already provides us with a convenient way to get the packet id so no need for reflection
        int id = EnumProtocol.PLAY.a(direction, nmsPacket);

        Class<? extends EPacket<?>> packetClass = (inbound ? serverBoundPackets : clientBoundPackets)[id];

        if (packetClass == null)
            return null;

        EPacket<?> packet = packetClass.newInstance();

        packet.setId(id);
        packet.setTimestamp(now);

        // This is the serializer we will use to read values
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        nmsPacket.b(serializer);

        packet.handle(serializer);

        return packet;
    }
}
