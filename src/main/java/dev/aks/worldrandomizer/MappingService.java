package dev.aks.worldrandomizer;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Биекция всех размещаемых блоков → блоков.
 * Детерминирована по сидy мира (worldSeed ^ SALT).
 */
public final class MappingService {
    private static final long SALT = 0xD15EA5E5CAFEL;

    private final Map<Identifier, Identifier> map = new HashMap<>();
    private final Map<Identifier, Identifier> inverse = new HashMap<>();

    public MappingService(long worldSeed) {
        List<Identifier> pool = Registries.BLOCK.stream()
                .filter(b -> !b.getDefaultState().isAir())
                .filter(MappingService::hasItemForm)
                .map(Registries.BLOCK::getId)
                .collect(Collectors.toCollection(ArrayList::new));

        Random rng = new Random(worldSeed ^ SALT);
        List<Identifier> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, rng);

        for (int i = 0; i < pool.size(); i++) {
            Identifier from = pool.get(i);
            Identifier to = shuffled.get(i);
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
        Identifier id = Registries.BLOCK.getId(block);
        return Registries.ITEM.containsId(id);
    }
}
