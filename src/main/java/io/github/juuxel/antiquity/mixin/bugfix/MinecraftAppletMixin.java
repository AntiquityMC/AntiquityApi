package io.github.juuxel.antiquity.mixin.bugfix;

import com.mojang.minecraft.MinecraftApplet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftApplet.class)
public abstract class MinecraftAppletMixin {
    @Shadow
    public abstract void startGameThread();

    @Inject(method = "init", at = @At("RETURN"))
    private void antiquity_onInit(CallbackInfo info) {
        startGameThread();
    }
}
