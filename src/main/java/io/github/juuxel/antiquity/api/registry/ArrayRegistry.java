package io.github.juuxel.antiquity.api.registry;

import io.github.juuxel.antiquity.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Registry implementation that also modifies an array.
 *
 * @param <T> the entry type
 */
// TODO: Registry saving to level.dat
public class ArrayRegistry<T> extends SimpleRegistry<T> {
    private final T[] array;

    public ArrayRegistry(T[] array, int startingId) {
        super(startingId);

        this.array = array;
    }

    public ArrayRegistry(T[] array) {
        super();

        this.array = array;
    }

    @Nullable
    @Override
    public T get(int rawId) {
        return (rawId >= 0 && rawId < array.length) ? array[rawId] : null;
    }

    @Override
    public <U extends T> U register(Identifier id, U entry) {
        super.register(id, entry);
        array[getRawId(entry)] = entry;

        return entry;
    }
}
