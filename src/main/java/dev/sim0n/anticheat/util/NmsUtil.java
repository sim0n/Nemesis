package dev.sim0n.anticheat.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@UtilityClass
public class NmsUtil {
    // Literally taken out of nms EntityLiving.class
    private static final UUID SPRINTING_SPEED_BOOST = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Sends a transaction packet to {@param player} with the {@param transactionId} id
     * @param player The player to send the transaction to
     * @param transactionId The id of the transaction packet
     */
    public void sendTransactionPacket(Player player, short transactionId) {
        PacketPlayOutTransaction packet = new PacketPlayOutTransaction(0, transactionId, false);

        getEntityPlayer(player).playerConnection.sendPacket(packet);
    }

    /**
     * Gets the player movement speed (without sprint)
     * @param player The player to get the movement speed of
     * @return The movement speed
     */
    public double getMovementSpeed(Player player) {
        EntityPlayer entityPlayer = getEntityPlayer(player);

        AttributeModifiable attribute = (AttributeModifiable) entityPlayer.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

        double baseValue = attribute.b();

        AtomicReference<Double> value = new AtomicReference<>(baseValue);

        IntStream.range(0, 3).forEach(i -> attribute.a(i)
                .forEach(modifier -> {
                    switch (i) {
                        case 0:
                            value.updateAndGet(v -> v + modifier.d());
                            break;

                        case 1:
                            value.updateAndGet(v -> v + modifier.d() * baseValue);
                            break;

                        case 2:
                            // We want to handle sprinting by ourselves considering it's shit in bukkit
                            if (!modifier.a().equals(SPRINTING_SPEED_BOOST))
                                value.updateAndGet(v -> v + v * modifier.d());
                            break;
                    }
                }));

        return value.get();
    }

    /**
     * Gets the friction of a block at {@param x} {@param y} {@param z}
     * @param world The world to check in
     * @param x The x location
     * @param y The y location
     * @param z The z location
     * @return The block friction
     */
    public float getBlockFriction(World world, double x, double y, double z) {
        return world.getType(new BlockPosition(x, y, z)).getBlock().frictionFactor;
    }
}
