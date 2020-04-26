package antiquity.mixin;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.DirtyChunkSorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Comparator;

@Mixin(DirtyChunkSorter.class)
public class DirtyChunkSorterMixin implements Comparator<Chunk> {
    @Shadow
    private Player player;

    @Override
    public int compare(Chunk first, Chunk second) {
        boolean firstVisible = first.field_286;
        boolean secondVisible = second.field_286;

        if (firstVisible && !secondVisible) {
            return -1;
        } else if (!firstVisible && secondVisible) {
            return 1;
        } else {
            return Float.compare(first.method_87(player), second.method_87(player));
        }
    }
}
