package dev.sim0n.anticheat.player.tracker.impl;

import dev.sim0n.anticheat.check.Check;
import dev.sim0n.anticheat.check.CheckData;
import dev.sim0n.anticheat.check.impl.pingspoof.PingSpoofB;
import dev.sim0n.anticheat.check.impl.pingspoof.PingSpoofB2;
import dev.sim0n.anticheat.net.packet.ClientBoundPacket;
import dev.sim0n.anticheat.net.packet.ServerBoundPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketConfirmTransaction;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketKeepAlive;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketConfirmTransaction;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketKeepAlive;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.util.ExpiringLruSet;
import dev.sim0n.anticheat.violation.impl.DetailedPlayerViolation;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.shorts.Short2LongArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2LongMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class PingTracker extends PlayerTracker {
    private static final int TRANSACTION_TIMEOUT = 20 * 15; // 15 seconds

    private final Int2LongMap expectedKeepAlives = new Int2LongArrayMap();
    private final Short2LongMap expectedTransactions = new Short2LongArrayMap();
    private final Short2ObjectMap<Consumer<Short>> scheduledTransactions = new Short2ObjectArrayMap<>();

    private final ExpiringLruSet<Long> invalidPings = new ExpiringLruSet<>(30000L); // 30 seconds

    private boolean acceptedKeepAlive;
    private long keepAlivePing;

    private boolean acceptedTransaction;
    private long transactionPing;

    public PingTracker(PlayerData playerData) {
        super(playerData);

        registerOutgoingPreHandler(SPacketKeepAlive.class, packet -> {
            expectedKeepAlives.put(packet.getKey(), packet.getTimestamp());
        }, ClientBoundPacket.PLAY_KEEP_ALIVE.getId());

        registerOutgoingPostHandler(SPacketConfirmTransaction.class, packet -> {
            short uid = packet.getUid();

            if (scheduledTransactions.containsKey(uid))
                return;

            // Let's not override our own entries
            if (expectedTransactions.containsKey(uid))
                expectedTransactions.put(uid, packet.getTimestamp());
        }, ClientBoundPacket.PLAY_WINDOW_TRANSACTION.getId());

        registerIncomingPreHandler(CPacketKeepAlive.class, packet -> {
            long timestamp = packet.getTimestamp();
            int key = packet.getKey();

            if (expectedKeepAlives.containsKey(key)) {
                keepAlivePing = timestamp - expectedKeepAlives.remove(key);
                acceptedKeepAlive = true;

                if (expectedTransactions.size() > TRANSACTION_TIMEOUT) {

                }
            } else {
                handleBadPing(timestamp, true, key);
            }

        }, ServerBoundPacket.PLAY_KEEP_ALIVE.getId());

        registerIncomingPreHandler(CPacketConfirmTransaction.class, packet -> {
            long timestamp = packet.getTimestamp();
            short uid = packet.getUid();

            if (expectedTransactions.containsKey(uid)) {
                transactionPing = timestamp - expectedTransactions.remove(uid);

                acceptedTransaction = true;
            } else if (scheduledTransactions.containsKey(uid)) {
                scheduledTransactions.remove(uid).accept(uid);
            } else {
                handleBadPing(timestamp, false, uid);
            }
        }, ServerBoundPacket.PLAY_WINDOW_TRANSACTION.getId());
    }

    /*
     * The client responds to keep alives that the server sent, therefore if they send one we haven't sent
     * they're cheating
     */
    private void handleBadPing(long timestamp, boolean keepAlive, int data) {
        invalidPings.add(timestamp);

        if (invalidPings.size() > 2) { // Need a threshold on this because mc is scuffed
            CheckData checkData = playerData.getCheckData();

            Check check = keepAlive ? checkData.getCheck(PingSpoofB.class) : checkData.getCheck(PingSpoofB2.class);

            check.handleViolation(new DetailedPlayerViolation(check, 1, "" + data));
        }
    }
}
