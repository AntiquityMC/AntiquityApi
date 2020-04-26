package io.github.juuxel.antiquity.mixin;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class AntiquityMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (mixinClassName.endsWith("SurvivalModeMixin")) {
            String superClassName = targetClassName.equals("com/mojang/minecraft/class_91") ? "com/mojang/minecraft/class_90" : "com/mojang/minecraft/gamemode/GameMode";
            MethodVisitor mv = targetClass.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lcom/mojang/minecraft/Minecraft;)V", null, null);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superClassName, "<init>", "(Lcom/mojang/minecraft/Minecraft;)V", false);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(2, 0);
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }
}
