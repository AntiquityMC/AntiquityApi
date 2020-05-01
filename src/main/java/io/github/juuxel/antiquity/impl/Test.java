package io.github.juuxel.antiquity.impl;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.tile.TileWithModel;
import io.github.juuxel.antiquity.api.util.Identifier;
import net.fabricmc.api.ModInitializer;

public final class Test implements ModInitializer {
    @Override
    public void onInitialize() {
        Tile rainbow = new TileWithModel(50, 0, new Identifier("antiquity", "rainbow"));
        Tile drawer = new TileWithModel(51, 0, new Identifier("antiquity", "drawer")) {
            @Override
            public boolean isSolid() {
                return false;
            }
        };
        User.creativeTiles.add(rainbow);
        User.creativeTiles.add(drawer);
    }
}
