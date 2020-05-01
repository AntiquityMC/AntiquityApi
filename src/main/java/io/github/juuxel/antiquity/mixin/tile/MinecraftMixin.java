package io.github.juuxel.antiquity.mixin.tile;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.class_71;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import io.github.juuxel.antiquity.api.tile.Direction;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Shadow
    public Level level;

    @Shadow
    public HitResult hitResult;

    @Shadow
    public Player player;

    @Shadow public class_71 field_644;

    @Inject(method = "handleMouseClick", at = @At(value = "FIELD", target = "Lcom/mojang/minecraft/HitResult;x:I"), cancellable = true)
    private void antiquity_onHandleMouseClick(int button, CallbackInfo info) {
        if (button == 1) {
            int x = hitResult.x;
            int y = hitResult.y;
            int z = hitResult.z;
            Tile tile = Tile.tiles[level.getTile(x, y, z)];
            if (ExtendedTile.of(tile).use(level, x, y, z, Direction.values()[hitResult.f], player)) {
                field_644.field_441.field_463 = true;
                info.cancel();
            }
        }
    }
}
