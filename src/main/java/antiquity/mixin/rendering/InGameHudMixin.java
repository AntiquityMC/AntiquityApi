package antiquity.mixin.rendering;

import antiquity.api.rendering.HudRenderCallback;
import com.mojang.minecraft.gui.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
abstract class InGameHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void antiquity_onRender(float f, boolean flag, int i, int j, CallbackInfo info) {
        HudRenderCallback.EVENT.invoker().render();
    }
}
