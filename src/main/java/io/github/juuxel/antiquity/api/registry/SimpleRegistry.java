package io.github.juuxel.antiquity.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.juuxel.antiquity.api.util.Identifier;
import io.github.juuxel.antiquity.api.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A simple bimap-based registry implementation.
 *
 * @param <T> the entry type
 */
public class SimpleRegistry<T> implements Registry<T> {
    private final BiMap<Identifier, T> entries = HashBiMap.create();
    private final BiMap<Integer, T> rawEntries = HashBiMap.create();
    private final Set<RegistrationListener<? super T>> listeners = new HashSet<>();

    private int nextId;

    public SimpleRegistry(int startingId) {
        this.nextId = startingId;
    }

    public SimpleRegistry() {
        this(0);
    }

    @Override
    public boolean contains(T entry) {
        return entries.containsValue(entry);
    }

    @Nullable
    @Override
    public T get(Identifier id) {
        return entries.get(id);
    }

    @Nullable
    @Override
    public T get(int rawId) {
        return rawEntries.get(rawId);
    }

    @Nullable
    @Override
    public Identifier getId(T entry) {
        return entries.inverse().get(entry);
    }

    @Override
    public int getRawId(T entry) {
        if (!rawEntries.containsKey(entry)) {
            throw new NoSuchElementException("Entry not registered: " + entry);
        }

        return rawEntries.inverse().get(entry);
    }

    @Override
    public <U extends T> U register(Identifier id, U entry) {
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("ID " + id + " has already been registered!");
        } else if (entries.containsValue(entry)) {
            throw new IllegalArgumentException("Value " + entry + " has already been registered!");
        }

        entries.put(id, entry);
        rawEntries.put(nextId++, entry);

        for (RegistrationListener<? super T> listener : listeners) {
            listener.onEntryRegistered(id, entry);
        }

        return entry;
    }

    @Override
    public void addRegistrationListener(RegistrationListener<? super T> listener) {
        listeners.add(listener);
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<Identifier, T>> iterator() {
        return entries.entrySet().iterator();
    }
}
