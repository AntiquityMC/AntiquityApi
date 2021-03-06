package io.github.juuxel.antiquity.api.rendering;

import io.github.juuxel.antiquity.api.util.Identifier;

import java.util.Objects;

public final class Texture {
    public static final Texture MISSINGNO = new Texture(new Identifier("antiquity", "missingno"), 0);
    public final String path;
    public final int index;

    public Texture(String path, int index) {
        this.path = Objects.requireNonNull(path, "path");
        this.index = index;
    }

    public Texture(Identifier id, int index) {
        this(convertToPath(id), index);
    }

    private static String convertToPath(Identifier id) {
        Objects.requireNonNull(id, "id");
        return id.getNamespace().equals("minecraft") ? ("/" + id.getPath() + ".png") : ("/assets/" + id.getNamespace() + "/textures/" + id.getPath() + ".png");
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
        if (texture.startsWith("#")) {
            String[] split = texture.substring(1).split("#");

            switch (split.length) {
                case 1:
                    return new Texture(texture, 0);
                case 2:
                    return new Texture("#" + split[0], Integer.parseInt(split[1]));
                default:
                    throw new IllegalArgumentException("Texture '" + texture + "' should contain at most two components!");
            }
        } else if (texture.contains("#")) {
            String[] split = texture.split("#");
            if (split.length != 2) {
                throw new IllegalArgumentException("Texture '" + texture + "' should contain at most two components!");
            }

            return new Texture(new Identifier(split[0]), Integer.parseInt(split[1]));
        } else {
            return new Texture(new Identifier(texture), 0);
        }
    }
}
