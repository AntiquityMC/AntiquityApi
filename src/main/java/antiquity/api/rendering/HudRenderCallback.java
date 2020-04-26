package antiquity.api.rendering;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface HudRenderCallback {
    Event<HudRenderCallback> EVENT = EventFactory.createArrayBacked(
            HudRenderCallback.class,
            listeners -> () -> {
                for (HudRenderCallback listener : listeners) {
                    listener.render();
                }
            }
    );

    void render();
}
