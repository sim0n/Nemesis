package dev.sim0n.anticheat.player.tracker.impl.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

@Getter
@RequiredArgsConstructor
public class TrackedPosition {
    private final long timestamp = System.currentTimeMillis();

    private final Vector position;
}
