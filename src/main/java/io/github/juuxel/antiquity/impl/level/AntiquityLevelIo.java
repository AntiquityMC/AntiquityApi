package io.github.juuxel.antiquity.impl.level;

import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import io.github.antiquitymc.nbt.ByteArrayTag;
import io.github.antiquitymc.nbt.CompoundTag;
import io.github.antiquitymc.nbt.ListTag;
import io.github.antiquitymc.nbt.NamedTag;
import io.github.antiquitymc.nbt.NbtIo;
import io.github.antiquitymc.nbt.ShortTag;
import io.github.antiquitymc.nbt.TagType;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import io.github.juuxel.antiquity.mixin.level.io.LevelIOAccessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class AntiquityLevelIo {
    private AntiquityLevelIo() {
    }

    @SuppressWarnings("unchecked")
    public static Level load(LevelIO io, File file) {
        //noinspection ConstantConditions
        LevelLoaderListener listener = ((LevelIOAccessor) (Object) io).getLevelLoaderListener();

        if (listener != null) {
            listener.beginLevelLoading("Loading level");
            listener.levelLoadUpdate("Reading...");
        }

        try (FileInputStream in = new FileInputStream(file)) {
            NamedTag namedRoot = NbtIo.read(in);

            if (!namedRoot.getName().equals("MinecraftLevel") || !(namedRoot.getTag() instanceof CompoundTag)) {
                throw new RuntimeException("Invalid level root tag!");
            }

            CompoundTag root = (CompoundTag) namedRoot.getTag();
            CompoundTag map = root.getSubTag("Map");

            short width = map.getShort("Width");
            short depth = map.getShort("Height");
            short height = map.getShort("Length"); // Notch uses "height" for Z
            ListTag<ShortTag> spawnPos = (ListTag<ShortTag>) map.get("Spawn", TagType.Standard.LIST);

            if (spawnPos.getElementType() != TagType.Standard.SHORT) {
                throw new RuntimeException("Illegal type for spawn: " + spawnPos.getElementType().getName());
            } else if (spawnPos.size() != 3) {
                throw new RuntimeException("Illegal size for spawn: " + spawnPos.size());
            }

            byte[] blocks = ((ByteArrayTag) map.get("Blocks")).getValue();
            byte[] states = ((ByteArrayTag) map.get("States")).getValue();

            CompoundTag about = root.getSubTag("About");

            String name = about.getString("Name");
            String author = about.getString("Author");
            long createdOn = about.getLong("CreatedOn");

            Level level = new Level();
            level.name = name;
            level.creator = author;
            level.createTime = createdOn;

            level.setData(width, height, depth, blocks);
            level.setSpawnPos(spawnPos.get(0).getValue(), spawnPos.get(1).getValue(), spawnPos.get(2).getValue(), 0);

            for (int i = 0; i < states.length; i++) {
                //noinspection ConstantConditions
                ((InternalExtendedLevel) level).setRawTileState(i, states[i]);
            }

            return level;
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static void save(Level level, File file) {
        try {
            CompoundTag root = new CompoundTag();
            CompoundTag about = new CompoundTag();
            about.putLong("CreatedOn", level.createTime);
            about.putString("Name", level.name);
            about.putString("Author", level.creator);

            CompoundTag environment = new CompoundTag();
            environment.putInt("SkyColor", level.skyColor);
            environment.putInt("CloudColor", level.cloudColor);

            CompoundTag map = new CompoundTag();
            map.putShort("Width", (short) level.width);
            map.putShort("Depth", (short) level.height); // Swapped
            map.putShort("Height", (short) level.depth);

            ListTag<ShortTag> spawn = new ListTag<>(TagType.Standard.SHORT);
            spawn.add(new ShortTag((short) level.xSpawn));
            spawn.add(new ShortTag((short) level.ySpawn));
            spawn.add(new ShortTag((short) level.zSpawn));

            map.put("Spawn", spawn);
            map.put("Blocks", new ByteArrayTag(level.blocks));

            TileState[] states = ((InternalExtendedLevel) level).getRawStates();
            byte[] stateIds = new byte[states.length];

            for (int i = 0; i < states.length; i++) {
                TileState state = states[i];
                if (state != null) {
                    stateIds[i] = state.getManager().indexOf(state);
                } else {
                    stateIds[i] = 0;
                }
            }

            map.put("States", new ByteArrayTag(stateIds));

            root.put("About", about);
            root.put("Environment", environment);
            root.put("Map", map);
            NamedTag namedRoot = new NamedTag("MinecraftLevel", root);

            try (FileOutputStream out = new FileOutputStream(file)) {
                NbtIo.write(out, namedRoot);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
