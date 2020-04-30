package io.github.juuxel.antiquity.api.rendering;

import io.github.juuxel.antiquity.api.tile.ExtendedTile;

import java.util.Objects;

public final class Texture {
    public static final Texture DEFAULT = new Texture(ExtendedTile.DEFAULT_TEXTURE, 0);
    public final String path;
    public final int index;

    public Texture(String path, int index) {
        this.path = Objects.requireNonNull(path, "path");
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Texture texture = (Texture) o;
        return index == texture.index &&
                path.equals(texture.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, index);
    }

    public static Texture fromString(String texture) {
        if (texture.contains(":")) {
            String[] split = texture.split(":");
            if (split.length != 2) {
                throw new IllegalArgumentException("Texture '" + texture + "' should contain at most one : character!");
            }

            return new Texture(split[0], Integer.parseInt(split[1]));
        } else {
            return new Texture(texture, 0);
        }
    }
}
