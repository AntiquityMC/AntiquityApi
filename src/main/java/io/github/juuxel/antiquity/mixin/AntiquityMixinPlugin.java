package io.github.juuxel.antiquity.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
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
            MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
            String superClassName = mappingResolver.mapClassName("intermediary", "com.mojang.minecraft.class_90").replace('.', '/');
            String minecraft = mappingResolver.mapClassName("intermediary", "com.mojang.minecraft.Minecraft").replace('.', '/');
            String descriptor = "(L" + minecraft + ";)V";

            MethodVisitor mv = targetClass.visitMethod(Opcodes.ACC_PUBLIC, "<init>", descriptor, null, null);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superClassName, "<init>", descriptor, false);
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
