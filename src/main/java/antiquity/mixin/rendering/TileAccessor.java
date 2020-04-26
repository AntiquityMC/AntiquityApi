package antiquity.mixin.rendering;

import com.mojang.minecraft.level.tile.Tile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Tile.class)
public interface TileAccessor {
    @Invoker
    int callGetTextureForSide(int side);
}
