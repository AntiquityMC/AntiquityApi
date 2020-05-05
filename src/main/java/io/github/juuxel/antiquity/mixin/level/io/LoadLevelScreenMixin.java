package io.github.juuxel.antiquity.mixin.level.io;

import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import io.github.juuxel.antiquity.impl.level.AntiquityLevelIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(LoadLevelScreen.class)
abstract class LoadLevelScreenMixin {
    @Redirect(method = "method_329", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/level/LevelIO;load(Ljava/io/File;)Lcom/mojang/minecraft/level/Level;"))
    private Level antiquity_onLoad(LevelIO io, File file) {
        return AntiquityLevelIo.load(io, file);
    }

    @Redirect(method = "buttonClicked", at = @At(value = "FIELD", target = "Lcom/mojang/minecraft/gui/LoadLevelScreen;field_561:Z", ordinal = 1))
    private boolean antiquity_onField_561(LoadLevelScreen self) {
        return false;
    }
}
