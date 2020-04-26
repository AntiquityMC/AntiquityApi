package antiquity.mixin.rendering;

import antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.tile.Tile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Item.class)
abstract class ItemMixin {
    @Shadow
    private int resource;

    @ModifyConstant(method = "render", constant = @Constant(stringValue = "/terrain.png"))
    private String modifyTexture(String texture) {
        return ExtendedTile.of(Tile.tiles[resource]).getTexturePath();
    }
}
