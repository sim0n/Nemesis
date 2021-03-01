package dev.sim0n.anticheat.util;

import dev.sim0n.anticheat.util.data.CustomLocation;
import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class BlockUtil {
    private final Set<Byte> CLIMBABLE = new HashSet<>(Arrays.asList((byte) 65, (byte) 106));
    private final Set<Byte> LIQUIDS = new HashSet<Byte>() {{
        for (int i = 8; i <= 11; i++) {
            add((byte) i);
        }
    }};

    /**
     * Gets all bounding boxes that collide with {@param boundingBox}
     * @param world The world to check in
     * @param boundingBox The bounding box to get all colliding bounding boxes from
     * @return The colliding bounding boxes
     */
    public Set<AxisAlignedBB> getCollidingBBs(World world, AxisAlignedBB boundingBox) {
        Set<AxisAlignedBB> bbs = new HashSet<>();

        int minX = MathHelper.floor(boundingBox.a);
        int maxX = MathHelper.floor(boundingBox.d);
        int minY = MathHelper.floor(boundingBox.b);
        int maxY = MathHelper.floor(boundingBox.e);
        int minZ = MathHelper.floor(boundingBox.c);
        int maxZ = MathHelper.floor(boundingBox.f);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                // We need to actually make sure the chunk is loaded
                if (world.isLoaded(new BlockPosition(x, 64, z))) {
                    // need to go 1 down for fences and walls
                    for (int y = minY - 1; y <= maxY; ++y) {
                        BlockPosition blockPosition = new BlockPosition(x, y, z);

                        Block block = world.getType(blockPosition).getBlock();

                        AxisAlignedBB blockBB = BBUtil.getBoundingBoxes()[CraftMagicNumbers.getId(block)];

                        if (blockBB != null) {
                            // Since NMS is stupid and doesn't use multiple bounding boxes we have to do this
                            blockBB = blockBB.c(x, y, z);
                        } else {
                            /*
                             * The default implementation of {@link Block#a(World, BlockPosition, IBlockData)} doesn't use
                             * the block data. However stuff like fences, etc will use the block data to determine places
                             * it connects to etc
                             */
                            blockBB = block.a(world, blockPosition, block.getBlockData());
                        }

                        // {@link AxisAlignedBB#b(AxisAlignedBB)} is a function to check if 2 bbs intersect
                        if (blockBB != null && boundingBox.b(blockBB)) {
                            bbs.add(blockBB);
                        }
                    }
                }
            }
        }

        return bbs;
    }

    /**
     * Checks if you're in liquid at {@param location}
     * @param world The world to check in
     * @param location The location to check at
     * @return If you're in liquid
     */
    public boolean isInLiquid(World world, CustomLocation location) {
        AxisAlignedBB boundingBox = location.toCollisionBox().getAABB();

        int minX = MathHelper.floor(boundingBox.a);
        int maxX = MathHelper.floor(boundingBox.d);
        int minY = MathHelper.floor(boundingBox.b);
        int maxY = MathHelper.floor(boundingBox.e);
        int minZ = MathHelper.floor(boundingBox.c);
        int maxZ = MathHelper.floor(boundingBox.f);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (world.isLoaded(new BlockPosition(x, 64, z))) {
                    for (int y = minY; y <= maxY; ++y) {
                        if (LIQUIDS.contains(getBlockId(world, x, y, z)))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if you're on a ladder (or well, climbable) at {@param location}
     * @param world The world to check in
     * @param location The location to check at
     * @return If you're on a ladder
     */
    public boolean isOnLadder(World world, CustomLocation location) {
        return CLIMBABLE.contains(getBlockId(world, location));
    }

    /**
     * Gets the id of a block in {@param world} at {@param x} {@param y} {@param z}
     * @param world The world to get the block id in
     * @param x The x location
     * @param y The y location
     * @param z The z location
     * @return The block id
     */
    private byte getBlockId(World world, int x, int y, int z) {
        Chunk chunk = world.getChunkAt(x >> 4, z >> 4);

        return (byte) Block.getId(chunk.getBlockData(new BlockPosition(x, y, z)).getBlock());
    }

    /**
     * Gets the id of a block in {@param world} at {@param location}
     * @param world The world to get the block id in
     * @param location The location to get the block at
     * @return The block id
     */
    private byte getBlockId(World world, CustomLocation location) {
        int x = MathHelper.floor(location.getX());
        int y = MathHelper.floor(location.getY());
        int z = MathHelper.floor(location.getZ());

        Chunk chunk = world.getChunkAt(x >> 4, z >> 4);

        return (byte) Block.getId(chunk.getBlockData(new BlockPosition(x, y, z)).getBlock());
    }
}
