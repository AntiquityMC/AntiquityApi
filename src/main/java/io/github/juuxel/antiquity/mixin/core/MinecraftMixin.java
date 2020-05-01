package io.github.juuxel.antiquity.mixin.core;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.MinecraftApplet;
import io.github.juuxel.antiquity.impl.core.MinecraftHolderImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void antiquity_onConstruct(Canvas canvas, MinecraftApplet applet, int width, int height, boolean fullscreen, CallbackInfo info) {
        MinecraftHolderImpl.minecraft = (Minecraft) (Object) this;
    }
}
