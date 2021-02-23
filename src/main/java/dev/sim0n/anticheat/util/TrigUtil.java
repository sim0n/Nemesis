package dev.sim0n.anticheat.util;

import dev.sim0n.anticheat.util.data.CustomLocation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrigUtil {

    /**
     * Gets the angle between {@param from} and {@param to} and subtracts with the direction of {@param to}
     * @param from The from location
     * @param to The to location
     * @return The move angle
     */
    public float getMoveAngle(CustomLocation from, CustomLocation to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        float moveAngle = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90F); // have to subtract by 90 because minecraft does it

        return Math.abs(wrapAngleTo180_float(moveAngle - to.getYaw()));
    }

    /**
     * Wrap a degree measure to 180 degrees. Used for yaw calculation in checks
     * @param value The input yaw value
     * @return The wrapped value
     */
    public float wrapAngleTo180_float(float value) {
        value %= 360F;

        if (value >= 180.0F)
            value -= 360.0F;

        if (value < -180.0F)
            value += 360.0F;

        return value;
    }

    /**
     * Wrap a degree measure to 180 degrees. Used for yaw calculation in checks
     * @param value The input yaw value
     * @return The wrapped value
     */
    public double wrapAngleTo180_double(double value) {
        value %= 360D;

        if (value >= 180D)
            value -= 360D;

        if (value < -180D)
            value += 360D;

        return value;
    }
}
