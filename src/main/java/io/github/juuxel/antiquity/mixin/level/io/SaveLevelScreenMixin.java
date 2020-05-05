package io.github.juuxel.antiquity.mixin.level.io;

import com.mojang.minecraft.User;
import com.mojang.minecraft.gui.SaveLevelScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import io.github.juuxel.antiquity.impl.level.AntiquityLevelIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(SaveLevelScreen.class)
abstract class SaveLevelScreenMixin {
    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lcom/mojang/minecraft/User;hasPaid:Z"))
    private boolean antiquity_onHasPaid(User user) {
        return true;
    }

    @Redirect(method = "method_329", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/level/LevelIO;save(Lcom/mojang/minecraft/level/Level;Ljava/io/File;)Z"))
    private boolean antiquity_onSave(LevelIO levelIO, Level level, File file) {
        AntiquityLevelIo.save(level, file);
        return true;
    }
}
