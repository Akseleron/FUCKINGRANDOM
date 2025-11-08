package dev.aks.worldrandomizer.chunk;

import dev.aks.worldrandomizer.MappingService;

/**
 * Заглушка. Когда решим реально переписывать чанки:
 * - Подписка на события загрузки чанков и тик сервера
 * - Тик-бюджетная замена блоков на mapping.mapBlockId(...)
 */
public final class ChunkRewriter {
    private final MappingService mapping;

    public ChunkRewriter(MappingService mapping) {
        this.mapping = mapping;
    }
}
