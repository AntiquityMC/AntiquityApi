package antiquity.mixin.rendering;

import antiquity.impl.rendering.TerrainAtlasImpl;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.BufferUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

@Mixin(Textures.class)
abstract class TexturesMixin {
    @Shadow
    public ByteBuffer field_422;

    @Unique
    private static ByteBuffer antiquity_cachedBuffer;

    @Redirect(method = "getTextureId", at = @At(value = "INVOKE", target = "Ljavax/imageio/ImageIO;read(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;"))
    private BufferedImage antiquity_replaceTerrainPng(InputStream input, String texture) throws IOException {
        if (texture.equals("/terrain.png")) {
            return TerrainAtlasImpl.stitch();
        } else {
            // Fall back to vanilla
            return ImageIO.read(input);
        }
    }

    @Redirect(method = "method_266", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;put([B)Ljava/nio/ByteBuffer;"))
    private ByteBuffer antiquity_checkBuffer(ByteBuffer buf, byte[] bytes) {
        try {
            return buf.put(bytes);
        } catch (BufferOverflowException e) {
            antiquity_cachedBuffer = buf;
            this.field_422 = BufferUtils.createByteBuffer(bytes.length);
            return field_422.put(bytes);
        }
    }

    @Inject(method = "getTextureId", at = @At(value = "RETURN", ordinal = 1))
    private void antiquity_afterTextureLoading(String path, CallbackInfoReturnable<Integer> info) {
        antiquity_restoreBuffer();
    }

    @Inject(method = "method_268", at = @At("RETURN"))
    private void antiquity_restoreBuffer(BufferedImage image, CallbackInfoReturnable<Integer> info) {
        antiquity_restoreBuffer();
    }

    @Unique
    private void antiquity_restoreBuffer() {
        if (antiquity_cachedBuffer != null) {
            this.field_422 = antiquity_cachedBuffer;
            antiquity_cachedBuffer = null;
        }
    }
}
