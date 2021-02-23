package dev.sim0n.anticheat.check;

import dev.sim0n.anticheat.player.PlayerData;
import lombok.Getter;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Getter
public class CheckManager {
    private final Set<Class<? extends Check>> checkClasses = new HashSet<>();
    private final Set<Constructor<?>> constructors = new HashSet<>();

    public CheckManager() {
        ClassIndex.getSubclasses(Check.class, Check.class.getClassLoader())
                .forEach(clazz -> {
                    if (Modifier.isAbstract(clazz.getModifiers()))
                        return;

                    checkClasses.add(clazz);
                });

        checkClasses.forEach(clazz -> {
            try {
                constructors.add(clazz.getConstructor(PlayerData.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }
}
