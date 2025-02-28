package net.paladins.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.spell_engine.api.config.ConfigFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ConfigFile.Equipment itemConfig;
    public final static StructurePoolConfig villageConfig;
    static {
        itemConfig = new ConfigFile.Equipment();
        villageConfig = new StructurePoolConfig();
        var weight = 3;
        var limit = 1;
        villageConfig.entries.addAll(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", "paladins:village/desert/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", "paladins:village/savanna/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", "paladins:village/plains/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", "paladins:village/taiga/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", "paladins:village/snowy/sanctuary", weight, limit)
        ));
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}