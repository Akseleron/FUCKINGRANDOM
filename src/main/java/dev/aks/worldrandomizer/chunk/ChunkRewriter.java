package dev.aks.worldrandomizer.chunk;

import dev.aks.worldrandomizer.MappingService;

/**
 * Заглушка. Когда решим реально переписывать чанки:
 * - В конструкторе подпишемся на ServerChunkEvents.CHUNK_LOAD
 * - В ServerTickEvents.END_SERVER_TICK будем выносить работу по частям (тик-бюджет)
 * - Каждую позицию заменяем на mapping.mapBlockId(...)
 */
public final class ChunkRewriter {
    private final MappingService mapping;

    public ChunkRewriter(MappingService mapping) {
        this.mapping = mapping;
    }
}
