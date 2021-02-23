package dev.sim0n.anticheat.player.tracker;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.net.packet.system.EPacket;
import dev.sim0n.anticheat.player.PlayerData;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.atteo.classindex.IndexSubclasses;

import java.util.function.Consumer;

@Getter
@IndexSubclasses
@RequiredArgsConstructor
public class PlayerTracker {
    protected final AntiCheat plugin = AntiCheat.getInstance();

    protected final PlayerData playerData;

    private final Int2ObjectMap<Consumer<EPacket<?>>> prePacketHandlers = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectMap<Consumer<EPacket<?>>> postPacketHandlers = new Int2ObjectLinkedOpenHashMap<>();

    private final Int2ObjectMap<Consumer<EPacket<?>>> outgoingPrePacketHandlers = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectMap<Consumer<EPacket<?>>> outgoingPostPacketHandlers = new Int2ObjectLinkedOpenHashMap<>();

    protected <T extends EPacket<?>> void registerIncomingPreHandler(Class<T> clazz, Consumer<T> consumer, int... ids) {
        for (int id : ids) {
            prePacketHandlers.put(id, (Consumer<EPacket<?>>) consumer);
        }
    }

    protected <T extends EPacket<?>> void registerIncomingPostHandler(Class<T> clazz, Consumer<T> consumer, int... ids) {
        for (int id : ids) {
            postPacketHandlers.put(id, (Consumer<EPacket<?>>) consumer);
        }
    }

    protected <T extends EPacket<?>> void registerOutgoingPreHandler(Class<T> clazz, Consumer<T> consumer, int... ids) {
        for (int id : ids) {
            outgoingPrePacketHandlers.put(id, (Consumer<EPacket<?>>) consumer);
        }
    }

    protected <T extends EPacket<?>> void registerOutgoingPostHandler(Class<T> clazz, Consumer<T> consumer, int... ids) {
        for (int id : ids) {
            outgoingPostPacketHandlers.put(id, (Consumer<EPacket<?>>) consumer);
        }
    }
}
