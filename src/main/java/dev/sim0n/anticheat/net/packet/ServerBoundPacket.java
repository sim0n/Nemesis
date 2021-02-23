package dev.sim0n.anticheat.net.packet;

import com.google.common.collect.BiMap;
import dev.sim0n.anticheat.util.ReflectionUtil;

import lombok.Getter;
import lombok.SneakyThrows;

import net.minecraft.server.v1_8_R3.*;

import java.util.Map;

// @see https://github.com/ProtocolSupport/ProtocolSupport/blob/9b6e7d6870888ed6834b8bea8aee09b791aa1965/src/protocolsupport/protocol/ServerBoundPacket.java
@Getter
public enum ServerBoundPacket {
    LOGIN_START(PacketLoginInStart.class),
    LOGIN_ENCRYPTION_BEGIN(PacketLoginInEncryptionBegin.class),
    PLAY_KEEP_ALIVE(PacketPlayInKeepAlive.class),
    PLAY_CHAT(PacketPlayInChat.class),
    PLAY_USE_ENTITY(PacketPlayInUseEntity.class),
    PLAY_PLAYER(PacketPlayInFlying.class),
    PLAY_POSITION(PacketPlayInFlying.PacketPlayInPosition.class),
    PLAY_LOOK(PacketPlayInFlying.PacketPlayInLook.class),
    PLAY_POSITION_LOOK(PacketPlayInFlying.PacketPlayInPositionLook.class),
    PLAY_BLOCK_DIG(PacketPlayInBlockDig.class),
    PLAY_BLOCK_PLACE(PacketPlayInBlockPlace.class),
    PLAY_HELD_SLOT(PacketPlayInHeldItemSlot.class),
    PLAY_ANIMATION(PacketPlayInArmAnimation.class),
    PLAY_ENTITY_ACTION(PacketPlayInEntityAction.class),
    PLAY_STEER_VEHICLE(PacketPlayInSteerVehicle.class),
    PLAY_WINDOW_CLOSE(PacketPlayInCloseWindow.class),
    PLAY_WINDOW_CLICK(PacketPlayInWindowClick.class),
    PLAY_WINDOW_TRANSACTION(PacketPlayInTransaction.class),
    PLAY_CREATIVE_SET_SLOT(PacketPlayInSetCreativeSlot.class),
    PLAY_ENCHANT_SELECT(PacketPlayInEnchantItem.class),
    PLAY_UPDATE_SIGN(PacketPlayInUpdateSign.class),
    PLAY_ABILITIES(PacketPlayInAbilities.class),
    PLAY_TAB_COMPLETE(PacketPlayInTabComplete.class),
    PLAY_SETTINGS(PacketPlayInSettings.class),
    PLAY_CLIENT_COMMAND(PacketPlayInClientCommand.class),
    PLAY_CUSTOM_PAYLOAD(PacketPlayInCustomPayload.class);

    final int id;

    @SneakyThrows
    ServerBoundPacket(Class<? extends Packet<?>> packetClass) {
        Map<Class<? extends Packet<?>>, EnumProtocol> protocolMap = (Map<Class<? extends Packet<?>>, EnumProtocol>) ReflectionUtil.setAccessible(EnumProtocol.class.getDeclaredField("h")).get(null);
        EnumProtocol protocol = protocolMap.get(packetClass);

        Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> idMap = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>>) ReflectionUtil.setAccessible(EnumProtocol.class.getDeclaredField("j")).get(protocol);

        id = idMap.get(EnumProtocolDirection.SERVERBOUND).inverse().get(packetClass);
    }
}
