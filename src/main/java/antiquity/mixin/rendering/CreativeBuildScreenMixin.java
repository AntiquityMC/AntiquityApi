package antiquity.mixin.rendering;

import antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.gui.CreativeBuildScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.tile.Tile;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CreativeBuildScreen.class)
abstract class CreativeBuildScreenMixin extends Screen {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object antiquity_onRender(List<?> list, int index) {
        Tile tile = (Tile) list.get(index);
        GL11.glBindTexture(3553, minecraft.textures.getTextureId(ExtendedTile.of(tile).getTexturePath()));
        return tile;
    }
}
