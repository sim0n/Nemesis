package dev.sim0n.anticheat.net.packet.system.impl.serverbound;

import dev.sim0n.anticheat.net.packet.system.EPacket;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.lang.ref.WeakReference;

@Getter
public class CPacketUseEntity extends EPacket<PacketPlayInUseEntity> {
    protected int entityId;

    protected EnumEntityUseAction action;

    protected Vector hitVec;

    protected WeakReference<Entity> entity;

    @Override
    public void handle(PacketDataSerializer serializer) {
        // PacketDataSerializer#e = readVarInt
        entityId = serializer.e();

        action = EnumEntityUseAction.values()[serializer.a(PacketPlayInUseEntity.EnumEntityUseAction.class).ordinal()];

        if (action == EnumEntityUseAction.INTERACT_AT) {
            hitVec = new Vector(serializer.readFloat(), serializer.readFloat(), serializer.readFloat());
        }
    }

    public Entity getEntity(World world) {
        if (entity == null) {
            net.minecraft.server.v1_8_R3.Entity entity = ((CraftWorld) world).getHandle().a(entityId);

            if (entity == null) {
                this.entity = new WeakReference<>(null);
            } else {
                this.entity = new WeakReference<>(entity.getBukkitEntity());
            }
        }

        return entity.get();
    }

    public enum EnumEntityUseAction {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
