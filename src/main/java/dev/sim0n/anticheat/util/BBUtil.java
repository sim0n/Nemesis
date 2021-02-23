package dev.sim0n.anticheat.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Material;

import java.util.Set;

@UtilityClass
public class BBUtil {
    private final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[256];

    public AxisAlignedBB[] getBoundingBoxes() {
        return BOUNDING_BOXES;
    }

    static {
        AxisAlignedBB fenceBB = AxisAlignedBB.a(0, 0, 0, 1, 1.5, 1);

        for (int i = 0; i < 256; i++) {
            Material material = Material.values()[i];

            if (material != null && material.name().contains("FENCE"))
                BOUNDING_BOXES[i] = fenceBB;
        }

        registerBoundingBox(Material.COBBLE_WALL, fenceBB);
        registerBoundingBox(Material.CARPET, AxisAlignedBB.a(0, 0, 0, 1, 0.0625, 1));
        registerBoundingBox(Material.SNOW, AxisAlignedBB.a(0, 0, 0, 1, 1, 1));
        registerBoundingBox(Material.BREWING_STAND, AxisAlignedBB.a(0, 0, 0, 1, 0.875, 1));
    }

    private void registerBoundingBox(Material material, AxisAlignedBB bb) {
        BOUNDING_BOXES[material.getId()] = bb;
    }

    /**
     * Checks if {@param from} is collided with {@param to}
     * @param from The from bounding box
     * @param to The to bounding box
     * @return If 2 bbs collide
     */
    public boolean isCollided(AxisAlignedBB from, AxisAlignedBB to) {
        return from.a <= to.d && from.d >= to.a &&
                from.b <= to.e && from.e >= to.b &&
                from.c <= to.f && from.f >= to.c;
    }

    /**
     * Checks if any bounding boxes collides below with {@param targetY}
     * @param bbs The bounding boxes to check
     * @param targetY The y to check
     * @return If collided below
     */
    public boolean isCollidedBelow(Set<AxisAlignedBB> bbs, double targetY) {
        return bbs.stream()
                /*
                 * Since our bounding box system isn't perfect we have to check the minY and see if it's less than
                 * our target y
                 */
                .map(b -> b.b <= targetY)
                .filter(Boolean::booleanValue)
                .findFirst()
                .orElse(false);
    }

    /**
     * Checks if any bounding boxes collides above with {@param targetY}
     * @param bbs The bounding boxes to check
     * @param targetY The y to check
     * @return If above below
     */
    public boolean isCollidedAbove(Set<AxisAlignedBB> bbs, double targetY) {
        return bbs.stream()
                .map(b -> b.e >= targetY)
                .filter(Boolean::booleanValue)
                .findFirst()
                .orElse(false);
    }

}
