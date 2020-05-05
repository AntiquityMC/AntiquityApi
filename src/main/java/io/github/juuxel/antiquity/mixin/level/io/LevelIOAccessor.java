package io.github.juuxel.antiquity.mixin.level.io;

import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.LevelIO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelIO.class)
public interface LevelIOAccessor {
    @Accessor
    LevelLoaderListener getLevelLoaderListener();
}
