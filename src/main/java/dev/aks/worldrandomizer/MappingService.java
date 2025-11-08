package dev.aks.worldrandomizer;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;

// Простая обертка, чтобы хранить биекцию BlockId -> BlockId
public final class MappingService {
    private final Map<Identifier, Identifier> map = new HashMap<>();
    private final Map<Identifier, Identifier> inverse = new HashMap<>();

    public void buildDeterministicMapping(long seed) {
        // Собираем список "валидных" блоков с предметной формой
        List<Identifier> pool = new ArrayList<>();
        for (Block b : Registries.BLOCK) {
            if (hasItemForm(b)) {
                pool.add(Registries.BLOCK.getId(b));
            }
        }
        // Детерминированная перестановка от seed
        Collections.shuffle(pool, new Random(seed));

        // Биекция 1:1
        for (int i = 0; i < pool.size(); i++) {
            Identifier from = pool.get(i);
            Identifier to = pool.get((i + 1) % pool.size()); // кольцевая перестановка
            map.put(from, to);
            inverse.put(to, from);
        }
    }

    public Identifier mapBlockId(Identifier block) {
        return map.getOrDefault(block, block);
    }

    public Identifier inverseMap(Identifier block) {
        return inverse.getOrDefault(block, block);
    }

    private static boolean hasItemForm(Block block) {
        // В 1.21 предметная форма у всего, что не воздух и не тех.блоки.
        // Точное правило можно уточнить позже, стартуем с простого фильтра:
        return Registries.ITEM.containsId(Registries.BLOCK.getId(block));
    }
}
