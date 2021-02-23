package dev.sim0n.anticheat.util;

import lombok.experimental.UtilityClass;

/**
 * This is copy and pasted from the Minecraft client to get the most accurate result
 */
@UtilityClass
public class MathHelper
{
    private final float[] SIN_TABLE = new float[65536];

    static
    {
        for (int i = 0; i < 65536; ++i)
        {
            SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0D / 65536.0D);
        }
    }

    public float sin(float p_76126_0_)
    {
        return SIN_TABLE[(int)(p_76126_0_ * 10430.378F) & 65535];
    }

    public float cos(float value)
    {
        return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

    public float sqrt_float(float value)
    {
        return (float)Math.sqrt(value);
    }

    public float sqrt_double(double value)
    {
        return (float)Math.sqrt(value);
    }

    public double clamp_double(double num, double min, double max)
    {
        return num < min ? min : (Math.min(num, max));
    }

    public int floor(double var0)
    {
        int var2 = (int)var0;

        return var0 < (double)var2 ? var2 - 1 : var2;
    }
}

