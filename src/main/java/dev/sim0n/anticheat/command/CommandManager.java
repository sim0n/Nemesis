package dev.sim0n.anticheat.command;

import lombok.Getter;
import org.atteo.classindex.ClassIndex;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CommandManager {
    private final Set<BaseCommand> commands = new HashSet<>();

    public CommandManager() {
        ClassIndex.getSubclasses(BaseCommand.class, BaseCommand.class.getClassLoader())
                .forEach(clazz -> {
                    try {
                        commands.add(clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }
}
