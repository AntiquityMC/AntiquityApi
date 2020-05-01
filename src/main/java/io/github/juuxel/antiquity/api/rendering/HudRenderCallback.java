package io.github.juuxel.antiquity.api.rendering;

import com.mojang.minecraft.Minecraft;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface HudRenderCallback {
    Event<HudRenderCallback> EVENT = EventFactory.createArrayBacked(
            HudRenderCallback.class,
            listeners -> (minecraft, width, height, mouseX, mouseY) -> {
                for (HudRenderCallback listener : listeners) {
                    listener.render(minecraft, width, height, mouseX, mouseY);
                }
            }
    );

    void render(Minecraft minecraft, int width, int height, int mouseX, int mouseY);
}
