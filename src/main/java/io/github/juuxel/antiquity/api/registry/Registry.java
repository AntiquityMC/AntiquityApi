package io.github.juuxel.antiquity.api.registry;

import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.util.Identifier;
import io.github.juuxel.antiquity.api.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public interface Registry<T> extends Iterable<Pair<Identifier, T>> {
    Registry<Tile> TILE = new ArrayRegistry<>(Tile.tiles, 50);

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

    void addRegistrationListener(RegistrationListener<? super T> listener);

    default void forEach(BiConsumer<? super Identifier, ? super T> consumer) {
        for (Pair<Identifier, T> pair : this) {
            consumer.accept(pair.getFirst(), pair.getSecond());
        }
    }
}
