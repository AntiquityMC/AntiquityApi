package io.github.juuxel.antiquity.api.rendering.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.minecraft.renderer.Tesselator;
import io.github.juuxel.antiquity.api.rendering.TerrainAtlas;
import io.github.juuxel.antiquity.api.rendering.Texture;
import io.github.juuxel.antiquity.api.tile.Direction;
import io.github.juuxel.antiquity.api.util.Identifier;
import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Model {
    public final @Nullable Model parent;
    public final Map<String, Texture> textures;
    public final Collection<Element> elements;
    private Set<String> resolvedTextures = null;

    public Model(@Nullable Model parent, Map<? extends String, ? extends Texture> textures, Collection<? extends Element> elements) {
        Objects.requireNonNull(textures, "textures");
        Objects.requireNonNull(elements, "elements");
        this.parent = parent;

        if (parent != null) {
            if (elements.isEmpty()) {
                this.elements = parent.elements;
            } else {
                this.elements = ImmutableList.copyOf(elements);
            }

            if (parent.textures.isEmpty()) {
                this.textures = ImmutableMap.copyOf(textures);
            } else if (textures.isEmpty()) {
                this.textures = parent.textures;
            } else {
                HashMap<String, Texture> builder = new HashMap<>(parent.textures);
                builder.putAll(textures);
                this.textures = ImmutableMap.copyOf(builder);
            }
        } else {
            this.textures = ImmutableMap.copyOf(textures);
            this.elements = ImmutableList.copyOf(elements);
        }
    }

    public Texture resolveTexture(String key) {
        // TODO: circular
        Texture value = textures.get(key);
        return value == null ? Texture.DEFAULT : (value.path.startsWith("#") ? resolveTexture(value.path) : value);
    }

    public Set<String> getResolvedTextures() {
        if (resolvedTextures == null) {
            ImmutableSet.Builder<String> builder = ImmutableSet.builder();

            for (String key : textures.keySet()) {
                builder.add(resolveTexture(key).path);
            }

            resolvedTextures = builder.build();
        }

        return resolvedTextures;
    }

    public boolean render(Tesselator tesselator, float x, float y, float z, Direction side, boolean shouldCull) {
        boolean rendered = false;

        for (Element element : elements) {
            float minX = x + element.minX / 16f;
            float maxX = x + element.maxX / 16f;
            float minY = y + element.minY / 16f;
            float maxY = y + element.maxY / 16f;
            float minZ = z + element.minZ / 16f;
            float maxZ = z + element.maxZ / 16f;

            Face face = element.faces.get(side);
            if (face == null) continue;

            if (face.cull && shouldCull) {
                continue;
            } else {
                rendered = true;
            }

            Texture texture = resolveTexture(face.texture);
            int texIndex = texture.index + TerrainAtlasImpl.getTextureOffset(texture.path);
            int texX = texIndex % 16 << 4;
            int texY = texIndex / 16 << 4;

            float u0 = (texX + face.u0) / 256f;
            float u1 = (texX + face.u1) / 256f;
            float atlasHeight = TerrainAtlas.INSTANCE.getHeight();
            float v0 = (texY + face.v0) / atlasHeight;
            float v1 = (texY + face.v1) / atlasHeight;

            switch (side) {
                case DOWN:
                    tesselator.vertexUV(minX, minY, maxZ, u0, v1);
                    tesselator.vertexUV(minX, minY, minZ, u0, v0);
                    tesselator.vertexUV(maxX, minY, minZ, u1, v0);
                    tesselator.vertexUV(maxX, minY, maxZ, u1, v1);
                    break;
                case UP:
                    tesselator.vertexUV(maxX, maxY, maxZ, u1, v1);
                    tesselator.vertexUV(maxX, maxY, minZ, u1, v0);
                    tesselator.vertexUV(minX, maxY, minZ, u0, v0);
                    tesselator.vertexUV(minX, maxY, maxZ, u0, v1);
                    break;
                case NORTH:
                    tesselator.vertexUV(minX, maxY, minZ, u1, v0);
                    tesselator.vertexUV(maxX, maxY, minZ, u0, v0);
                    tesselator.vertexUV(maxX, minY, minZ, u0, v1);
                    tesselator.vertexUV(minX, minY, minZ, u1, v1);
                    break;
                case SOUTH:
                    tesselator.vertexUV(minX, maxY, maxZ, u0, v0);
                    tesselator.vertexUV(minX, minY, maxZ, u0, v1);
                    tesselator.vertexUV(maxX, minY, maxZ, u1, v1);
                    tesselator.vertexUV(maxX, maxY, maxZ, u1, v0);
                    break;
                case WEST:
                    tesselator.vertexUV(minX, maxY, maxZ, u1, v0);
                    tesselator.vertexUV(minX, maxY, minZ, u0, v0);
                    tesselator.vertexUV(minX, minY, minZ, u0, v1);
                    tesselator.vertexUV(minX, minY, maxZ, u1, v1);
                    break;
                case EAST:
                    tesselator.vertexUV(maxX, minY, maxZ, u0, v1);
                    tesselator.vertexUV(maxX, minY, minZ, u1, v1);
                    tesselator.vertexUV(maxX, maxY, minZ, u1, v0);
                    tesselator.vertexUV(maxX, maxY, maxZ, u0, v0);
                    break;
            }
        }

        return rendered;
    }

    public static Model fromJson(JsonObject json) {
        Identifier parentId = json.has("parent") ? new Identifier(json.getAsJsonPrimitive("parent").getAsString()) : null;
        Model parent = parentId != null ? ModelLoader.getModel(parentId) : null; // TODO: Fix circular dep
        List<Element> elements;

        if (json.has("elements")) {
            elements = new ArrayList<>();

            for (JsonElement element : json.getAsJsonArray("elements")) {
                elements.add(Element.fromJson(element.getAsJsonObject()));
            }
        } else {
            elements = ImmutableList.of();
        }

        Map<String, Texture> textures;

        if (json.has("textures")) {
            textures = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("textures").entrySet()) {
                textures.put("#" + entry.getKey(), Texture.fromString(entry.getValue().getAsString()));
            }
        } else {
            textures = ImmutableMap.of();
        }

        return new Model(parent, textures, elements);
    }

    public static final class Element {
        public final float minX, minY, minZ;
        public final float maxX, maxY, maxZ;
        public final Map<Direction, Face> faces;

        public Element(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Map<? extends Direction, ? extends Face> faces) {
            Objects.requireNonNull(faces, "faces");
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.faces = ImmutableMap.copyOf(faces);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return Float.compare(element.minX, minX) == 0 &&
                    Float.compare(element.minY, minY) == 0 &&
                    Float.compare(element.minZ, minZ) == 0 &&
                    Float.compare(element.maxX, maxX) == 0 &&
                    Float.compare(element.maxY, maxY) == 0 &&
                    Float.compare(element.maxZ, maxZ) == 0 &&
                    faces.equals(element.faces);
        }

        @Override
        public int hashCode() {
            return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ, faces);
        }

        private static void checkPositionArray(String name, JsonArray array) {
            if (array.size() != 3) {
                throw new IllegalArgumentException("Position array '" + name + "' must have size 3, found " + array.size());
            }
        }

        public static Element fromJson(JsonObject json) {
            JsonArray from = json.getAsJsonArray("from");
            checkPositionArray("from", from);
            JsonArray to = json.getAsJsonArray("to");
            checkPositionArray("to", to);

            Map<Direction, Face> faces;

            if (json.has("faces")) {
                faces = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("faces").entrySet()) {
                    Direction direction = Direction.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
                    faces.put(direction, Face.fromJson(entry.getValue().getAsJsonObject()));
                }
            } else {
                faces = ImmutableMap.of();
            }

            return new Element(
                    from.get(0).getAsFloat(),
                    from.get(1).getAsFloat(),
                    from.get(2).getAsFloat(),
                    to.get(0).getAsFloat(),
                    to.get(1).getAsFloat(),
                    to.get(2).getAsFloat(),
                    faces
            );
        }
    }

    public static final class Face {
        public final String texture;
        public final boolean cull;
        public final float u0, v0, u1, v1;

        public Face(String texture, float u0, float v0, float u1, float v1, boolean cull) {
            this.texture = Objects.requireNonNull(texture, "texture");
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            this.cull = cull;
        }

        public Face(String texture, float u0, float v0, float u1, float v1) {
            this(texture, u0, v0, u1, v1, false);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Face face = (Face) o;
            return cull == face.cull &&
                    texture.equals(face.texture);
        }

        @Override
        public int hashCode() {
            return Objects.hash(texture, cull);
        }

        public static Face fromJson(JsonObject json) {
            String texture = json.getAsJsonPrimitive("texture").getAsString();

            if (!texture.startsWith("#")) {
                throw new IllegalArgumentException("Texture variable '" + texture + "' should start with '#'!");
            }

            float u0, v0, u1, v1;

            if (json.has("uv")) {
                JsonArray uv = json.getAsJsonArray("uv");

                if (uv.size() != 4) {
                    throw new IllegalArgumentException("'uv' array size must be 4, found " + uv.size());
                }

                u0 = uv.get(0).getAsFloat();
                u1 = uv.get(1).getAsFloat();
                v0 = uv.get(2).getAsFloat();
                v1 = uv.get(3).getAsFloat();
            } else {
                // TODO: UV calculation
                u0 = 0;
                u1 = 16;
                v0 = 0;
                v1 = 16;
            }

            return json.has("cull")
                    ? new Face(texture, u0, v0, u1, v1, json.getAsJsonPrimitive("cull").getAsBoolean())
                    : new Face(texture, u0, v0, u1, v1);
        }
    }

}
