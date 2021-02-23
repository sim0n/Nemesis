package dev.sim0n.anticheat.violation.punishment;

import dev.sim0n.anticheat.player.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerBan {
    private final PlayerData playerData;

    private final String reason;
}
