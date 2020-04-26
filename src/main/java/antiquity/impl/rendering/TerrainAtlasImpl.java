package antiquity.impl.rendering;

import antiquity.api.rendering.TerrainAtlas;
import antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Textures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TerrainAtlasImpl implements TerrainAtlas {
    INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<String> paths = new ArrayList<>();
    private static final Map<String, Integer> textureOffsets = new HashMap<>();
    private static int atlasHeight;

    static {
        paths.add("/terrain.png");
    }

    @Override
    public void add(String path) {
        if (!paths.contains(path)) {
            paths.add(path);
        }
    }

    @Override
    public float getHeight() {
        return atlasHeight;
    }


    public static BufferedImage stitch() throws IOException {
        LOGGER.info("Scanning terrain atlas inputs from tiles");

        for (Tile tile : Tile.tiles) {
            if (tile == null) continue;
            INSTANCE.add(ExtendedTile.of(tile).getTexturePath());
        }

        LOGGER.info("Stitching terrain atlas from {} entries", paths.size());
        List<BufferedImage> images = new ArrayList<>();
        int totalHeight = 0;

        for (String path : paths) {
            try (InputStream in = Textures.class.getResourceAsStream(path)) {
                BufferedImage image = ImageIO.read(in);
                images.add(image);
                textureOffsets.put(path, totalHeight / 16 * 16);
                totalHeight += image.getHeight();
            }
        }

        // Store the height for later use
        atlasHeight = totalHeight;

        BufferedImage result = new BufferedImage(256, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.createGraphics();

        int y = 0;
        for (BufferedImage image : images) {
            graphics.drawImage(image, 0, y, 256, image.getHeight(), null);
            y += image.getHeight();
        }

        graphics.dispose();
        ImageIO.write(result, "PNG", new File("atlas.png"));
        return result;
    }

    public static int getTextureOffset(String texture) {
        return textureOffsets.get(texture);
    }
}
