package dev.sim0n.anticheat.net.packet;

import com.google.common.collect.BiMap;
import dev.sim0n.anticheat.util.ReflectionUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.*;

import java.util.Map;

// @see https://github.com/ProtocolSupport/ProtocolSupport/blob/9b6e7d6870888ed6834b8bea8aee09b791aa1965/src/protocolsupport/protocol/ClientBoundPacket.java
@Getter
public enum ClientBoundPacket {
    LOGIN_DISCONNECT(PacketLoginOutDisconnect.class),
    LOGIN_ENCRYPTION_BEGIN(PacketLoginOutEncryptionBegin.class),
    LOGIN_SUCCESS(PacketLoginOutSuccess.class),
    STATUS_SERVER_INFO(PacketStatusOutServerInfo.class),
    STATUS_PONG(PacketStatusOutPong.class),
    PLAY_KEEP_ALIVE(PacketPlayOutKeepAlive.class),
    PLAY_LOGIN(PacketPlayOutLogin.class),
    PLAY_CHAT(PacketPlayOutChat.class),
    PLAY_UPDATE_TIME(PacketPlayOutUpdateTime.class),
    PLAY_ENTITY_EQUIPMENT(PacketPlayOutEntityEquipment.class),
    PLAY_SPAWN_POSITION(PacketPlayOutSpawnPosition.class),
    PLAY_UPDATE_HEALTH(PacketPlayOutUpdateHealth.class),
    PLAY_RESPAWN(PacketPlayOutRespawn.class),
    PLAY_POSITION(PacketPlayOutPosition.class),
    PLAY_HELD_SLOT(PacketPlayOutHeldItemSlot.class),
    PLAY_BED(PacketPlayOutBed.class),
    PLAY_ANIMATION(PacketPlayOutAnimation.class),
    PLAY_SPAWN_NAMED(PacketPlayOutNamedEntitySpawn.class),
    PLAY_COLLECT_EFFECT(PacketPlayOutCollect.class),
    PLAY_SPAWN_OBJECT(PacketPlayOutSpawnEntity.class),
    PLAY_SPAWN_LIVING(PacketPlayOutSpawnEntityLiving.class),
    PLAY_SPAWN_PAINTING(PacketPlayOutSpawnEntityPainting.class),
    PLAY_SPAWN_EXP_ORB(PacketPlayOutSpawnEntityExperienceOrb.class),
    PLAY_ENTITY_VELOCITY(PacketPlayOutEntityVelocity.class),
    PLAY_ENTITY_DESTROY(PacketPlayOutEntityDestroy.class),
    PLAY_ENTITY(PacketPlayOutEntity.class),
    PLAY_ENTITY_REL_MOVE(PacketPlayOutEntity.PacketPlayOutRelEntityMove.class),
    PLAY_ENTITY_LOOK(PacketPlayOutEntity.PacketPlayOutEntityLook.class),
    PLAY_ENTITY_REL_MOVE_LOOK(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class),
    PLAY_ENTITY_TELEPORT(PacketPlayOutEntityTeleport.class),
    PLAY_ENTITY_HEAD_ROTATION(PacketPlayOutEntityHeadRotation.class),
    PLAY_ENTITY_STATUS(PacketPlayOutEntityStatus.class),
    PLAY_ENTITY_ATTACH(PacketPlayOutAttachEntity.class),
    PLAY_ENTITY_METADATA(PacketPlayOutEntityMetadata.class),
    PLAY_ENTITY_EFFECT_ADD(PacketPlayOutEntityEffect.class),
    PLAY_ENTITY_EFFECT_REMOVE(PacketPlayOutRemoveEntityEffect.class),
    PLAY_EXPERIENCE(PacketPlayOutExperience.class),
    PLAY_ENTITY_ATTRIBUTES(PacketPlayOutUpdateAttributes.class),
    PLAY_CHUNK_SINGLE(PacketPlayOutMapChunk.class),
    PLAY_BLOCK_CHANGE_MULTI(PacketPlayOutMultiBlockChange.class),
    PLAY_BLOCK_CHANGE_SINGLE(PacketPlayOutBlockChange.class),
    PLAY_BLOCK_ACTION(PacketPlayOutBlockAction.class),
    PLAY_BLOCK_BREAK_ANIMATION(PacketPlayOutBlockBreakAnimation.class),
    PLAY_CHUNK_MULTI(PacketPlayOutMapChunkBulk.class),
    PLAY_EXPLOSION(PacketPlayOutExplosion.class),
    PLAY_WORLD_EVENT(PacketPlayOutWorldEvent.class),
    PLAY_WORLD_SOUND(PacketPlayOutNamedSoundEffect.class),
    PLAY_WORLD_PARTICLES(PacketPlayOutWorldParticles.class),
    PLAY_GAME_STATE_CHANGE(PacketPlayOutGameStateChange.class),
    PLAY_SPAWN_WEATHER(PacketPlayOutSpawnEntityWeather.class),
    PLAY_WINDOW_OPEN(PacketPlayOutOpenWindow.class),
    PLAY_WINDOW_CLOSE(PacketPlayOutCloseWindow.class),
    PLAY_WINDOW_SET_SLOT(PacketPlayOutSetSlot.class),
    PLAY_WINDOW_SET_ITEMS(PacketPlayOutWindowItems.class),
    PLAY_WINDOW_DATA(PacketPlayOutWindowData.class),
    PLAY_WINDOW_TRANSACTION(PacketPlayOutTransaction.class),
    PLAY_UPDATE_SIGN(PacketPlayOutUpdateSign.class),
    PLAY_MAP(PacketPlayOutMap.class),
    PLAY_UPDATE_TILE(PacketPlayOutTileEntityData.class),
    PLAY_SIGN_EDITOR(PacketPlayOutOpenSignEditor.class),
    PLAY_STATISTICS(PacketPlayOutStatistic.class),
    PLAY_PLAYER_INFO(PacketPlayOutPlayerInfo.class),
    PLAY_ABILITIES(PacketPlayOutAbilities.class),
    PLAY_TAB_COMPLETE(PacketPlayOutTabComplete.class),
    PLAY_SCOREBOARD_OBJECTIVE(PacketPlayOutScoreboardObjective.class),
    PLAY_SCOREBOARD_SCORE(PacketPlayOutScoreboardScore.class),
    PLAY_SCOREBOARD_DISPLAY_SLOT(PacketPlayOutScoreboardDisplayObjective.class),
    PLAY_SCOREBOARD_TEAM(PacketPlayOutScoreboardTeam.class),
    PLAY_CUSTOM_PAYLOAD(PacketPlayOutCustomPayload.class),
    PLAY_KICK_DISCONNECT(PacketPlayOutKickDisconnect.class),
    PLAY_RESOURCE_PACK(PacketPlayOutResourcePackSend.class);

    final int id;

    @SneakyThrows
    ClientBoundPacket(Class<?> packetClass) {
        Map<Class<? extends Packet<?>>, EnumProtocol> protocolMap = (Map<Class<? extends Packet<?>>, EnumProtocol>) ReflectionUtil.setAccessible(EnumProtocol.class.getDeclaredField("h")).get(null);
        EnumProtocol protocol = protocolMap.get(packetClass);

        Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> idMap = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>>) ReflectionUtil.setAccessible(EnumProtocol.class.getDeclaredField("j")).get(protocol);

        id = idMap.get(EnumProtocolDirection.CLIENTBOUND).inverse().get(packetClass);
    }

}
