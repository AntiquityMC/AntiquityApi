package io.github.juuxel.antiquity.mixin.rendering;

import com.mojang.minecraft.Minecraft;
import io.github.juuxel.antiquity.api.rendering.HudRenderCallback;
import com.mojang.minecraft.gui.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
abstract class InGameHudMixin {
    @Shadow
    private Minecraft minecraft;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Inject(method = "render", at = @At("HEAD"))
    private void antiquity_onRender(float f, boolean hasScreen, int mouseX, int mouseY, CallbackInfo info) {
        HudRenderCallback.EVENT.invoker().render(minecraft, scaledWidth, scaledHeight, mouseX, mouseY);
    }
}
