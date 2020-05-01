package io.github.juuxel.antiquity.api.core;

import com.mojang.minecraft.Minecraft;
import io.github.juuxel.antiquity.impl.core.MinecraftHolderImpl;

public final class MinecraftHolder {
    private MinecraftHolder() {}

    public static Minecraft get() {
        return MinecraftHolderImpl.minecraft;
    }
}
