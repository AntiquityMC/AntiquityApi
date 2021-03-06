package io.github.juuxel.antiquity.api.registry;

import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

public interface Registry<T> extends Iterable<Map.Entry<Identifier, T>> {
    // TODO: Add vanilla entries?
    Registry<Tile> TILE = new ArrayRegistry<>(Tile.tiles, 50);

    /**
     * Returns whether the entry is registered in this registry.
     *
     * @param entry the entry
     * @return true if the entry is registered, false otherwise
     */
    boolean contains(T entry);

    /**
     * Gets the entry associated with the ID.
     *
     * @param id the ID
     * @return the entry, or null if not found
     */
    @Nullable
    T get(Identifier id);

    /**
     * Gets the entry associated with the raw ID.
     *
     * @param rawId the raw ID
     * @return the entry, or null if not found
     */
    @Nullable
    T get(int rawId);

    /**
     * Gets the ID associated with the entry.
     *
     * @param entry the entry
     * @return the entry's ID, or null if not found
     */
    @Nullable
    Identifier getId(T entry);

    /**
     * Gets the integer ID of the entry.
     *
     * @param entry the entry
     * @return its integer ID
     * @throws java.util.NoSuchElementException if the entry has not been registered
     */
    int getRawId(T entry);

    /**
     * Registers the entry to the registry.
     *
     * <p>This method also notifies all registration listeners for this registry.
     *
     * @param id    the ID
     * @param entry the entry
     * @param <U>   the entry type
     * @return the entry
     * @throws IllegalArgumentException if the ID or the entry has already been registered
     */
    <U extends T> U register(Identifier id, U entry);

    /**
     * Registers a computed entry to the registry.
     * The entry computation function is passed the raw ID of the entry.
     *
     * <p>This method also notifies all registration listeners for this registry.
     *
     * @param id            the ID
     * @param entryFunction the entry function
     * @param <U>           the entry type
     * @return the entry
     * @throws IllegalArgumentException if the ID or the entry has already been registered
     */
    <U extends T> U register(Identifier id, IntFunction<? extends U> entryFunction);

    /**
     * Adds a registration listener that will be notified each time a new entry is registered.
     *
     * @param listener the listener
     */
    void addRegistrationListener(RegistrationListener<? super T> listener);

    default void forEach(BiConsumer<? super Identifier, ? super T> consumer) {
        for (Map.Entry<Identifier, T> entry : this) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
