package dev.sim0n.anticheat.util.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.util.Vector;

@Data
@AllArgsConstructor
public class CustomLocation {
    private long timestamp;

    private double x, y, z;

    private float yaw, pitch;

    private boolean onGround;

    public CustomLocation(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;

        this.timestamp = System.currentTimeMillis();
    }

    public CustomLocation(double x, double y, double z, float yaw, float pitch, boolean onGround, long timestamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;

        this.timestamp = timestamp;
    }

    public double distanceSquared(CustomLocation location) {
        double dx = x - location.x;
        double dy = y - location.y;
        double dz = z - location.z;

        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(CustomLocation location) {
        double dx = x - location.x;
        double dy = y - location.y;
        double dz = z - location.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * @return A vector pointing in the direction that the location is facing
     */
    public Vector getDirection() {
        double y = -Math.sin(Math.toRadians(pitch));

        double xz = Math.cos(Math.toRadians(pitch));

        return new Vector(
                -xz * Math.sin(Math.toRadians(yaw)),
                y,
                xz * Math.cos(Math.toRadians(yaw))).normalize();
    }

    public BoundingBox toBB(double width, double height) {
        return new BoundingBox(
                new Vector(x - width, y, z - width),
                new Vector(x + width, y + height, z + width));
    }

    public BoundingBox toBB(double width, double depth, double height) {
        return new BoundingBox(
                new Vector(x - width, y - depth, z - width),
                new Vector(x + width, y + height, z + width));
    }

    // Minecraft offsets the bounding box by 0.001 to do block collisions so we have to do the same
    public BoundingBox toCollisionBox() {
        return toBB(0.3, 1.8)
                .expand(0.001);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public CustomLocation clone() {
        return new CustomLocation(x, y, z, yaw, pitch, onGround, timestamp);
    }
}
