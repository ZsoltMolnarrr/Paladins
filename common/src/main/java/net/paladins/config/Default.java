package net.paladins.config;

import net.spell_engine.rpg_series.config.ConfigFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ConfigFile.Equipment itemConfig;
    static {
        itemConfig = new ConfigFile.Equipment();
    }
    // The village structure-pool defaults moved to `net.paladins.fabric.village.FabricVillageStructures`
    // (StructurePoolAPI is Fabric-only on 1.20.1).

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
