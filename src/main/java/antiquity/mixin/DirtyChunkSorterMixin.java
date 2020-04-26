package antiquity.mixin;

import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.DirtyChunkSorter;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Comparator;

@Mixin(DirtyChunkSorter.class)
public class DirtyChunkSorterMixin implements Comparator<Chunk> {
    @Override
    public int compare(Chunk first, Chunk second) {
        // TODO: Implement this based on 13a
        return 0;
    }
}
