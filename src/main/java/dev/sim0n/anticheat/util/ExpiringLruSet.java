package dev.sim0n.anticheat.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public class ExpiringLruSet<E> extends AbstractSet<E> implements Set<E>, Serializable {
    private static final Object PRESENT = new Object();

    private final ConcurrentMap<E, Object> map;
    private final Cache<E, Object> cache;

    public ExpiringLruSet(long expireMillis) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireMillis, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<Object, Object>() {
                    public Object load(Object o) throws Exception {
                        return PRESENT;
                    }
                });

        this.map = this.cache.asMap();
    }

    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override
    public int size() {
        this.cache.cleanUp();
        return this.map.size();
    }

    @Override
    public boolean add(E e) {
        return this.map.put(e, ExpiringLruSet.PRESENT) == null;
    }

}
