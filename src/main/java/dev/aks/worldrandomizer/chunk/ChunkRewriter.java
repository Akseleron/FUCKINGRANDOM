package dev.aks.worldrandomizer;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

// Низкоуровневая подмена состояний блоков внутри чанка
public final class ChunkRewriter {
    private final MappingService mapping;

    public ChunkRewriter(MappingService mapping) {
        this.mapping = mapping;
    }

    public void rewrite(Chunk chunk) {
        for (ChunkSection section : chunk.getSectionArray()) {
            if (section == null || section.isEmpty()) continue;

            var palette = section.getBlockStateContainer();
            var states = palette.getBackingArray();
            for (int i = 0; i < states.length; i++) {
                BlockState state = states[i];
                Identifier id = Registries.BLOCK.getId(state.getBlock());
                Identifier mapped = mapping.mapBlockId(id);
                if (!mapped.equals(id)) {
                    var newBlock = Registries.BLOCK.get(mapped);
                    if (newBlock != null) {
                        states[i] = newBlock.getDefaultState();
                    }
                }
            }
        }
        // Обновим освещение/статусы если потребуется — можно допилить позднее
        chunk.setNeedsSaving(true);
    }
}
