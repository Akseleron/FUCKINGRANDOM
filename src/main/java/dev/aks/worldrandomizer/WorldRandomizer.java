package dev.aks.worldrandomizer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Запускает маппинг при старте сервера и подменяет лут-таблицы блоков.
 */
public final class WorldRandomizer implements ModInitializer {
    public static final String MOD_ID = "world_randomizer";
    private static MappingService mapping;

    @Override
    public void onInitialize() {
        // Создаём детерминированную карту блок→блок на старте сервера
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            long seed = server.getOverworld().getSeed();
            mapping = new MappingService(seed);
        });

        // Подмена дропа всех ванильных блоков на целевой блок из маппинга
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (!"minecraft".equals(id.getNamespace())) return;
            String path = id.getPath();
            if (!path.startsWith("blocks/")) return;

            String blockPath = path.substring("blocks/".length());
            Identifier blockId = Identifier.of("minecraft", blockPath);
            if (!Registries.BLOCK.containsId(blockId)) return;

            Identifier targetId = mapping != null ? mapping.mapBlockId(blockId) : blockId;
            Item dropItem = Registries.ITEM.get(targetId);

            if (!(dropItem instanceof BlockItem)) {
                // если у целевого нет предметной формы — падает исходный
                dropItem = Registries.ITEM.get(blockId);
                if (dropItem == Items.AIR) return;
            }

            tableBuilder.pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(dropItem))
            );
        });

        // Если понадобится, тут подключим ChunkRewriter.
    }
}
