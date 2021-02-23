package dev.sim0n.anticheat.util;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.command.BaseCommand;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

@UtilityClass
public class CommandUtil {
    private final CommandMap commandMap = ReflectionUtil.getFieldValue(ReflectionUtil.getField(Bukkit.getServer().getClass(), "commandMap"), Bukkit.getServer());

    public void registerCommand(BaseCommand command) {
        try {
            commandMap.register(AntiCheat.getInstance().getName(), command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
