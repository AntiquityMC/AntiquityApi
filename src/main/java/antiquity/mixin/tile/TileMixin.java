package antiquity.mixin.tile;

import antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.level.tile.Tile;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Tile.class)
abstract class TileMixin implements ExtendedTile {
}
