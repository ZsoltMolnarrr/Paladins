package net.paladins.fabric.village;

import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.paladins.PaladinsMod;
import net.paladins.village.VillageStructures;
import net.spell_engine.Platform;
import net.tiny_config.ConfigManager;

import java.util.List;

/// Fabric-only implementation of {@link VillageStructures}: StructurePoolAPI has no Forge artifact on
/// 1.20.1, so both the `config/paladins/villages.json` config and the injection call live here.
public final class FabricVillageStructures {
    private FabricVillageStructures() { }

    public static final ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<StructurePoolConfig>
            ("villages", defaults())
            .builder()
            .setDirectory(PaladinsMod.ID)
            .sanitize(true)
            .build();

    /// Installs the injector and loads (or writes) the config file. Called from the Fabric entrypoint
    /// before {@code PaladinsMod.registerVillagers()}.
    public static void install() {
        villageConfig.refresh();
        VillageStructures.injector = () -> {
            if (!Platform.util().isModLoaded("lithostitched")) {
                // Only inject the village if Lithostitched is not present
                StructurePoolAPI.injectAll(villageConfig.value);
            }
        };
    }

    private static StructurePoolConfig defaults() {
        var config = new StructurePoolConfig();
        var weight = 3;
        var limit = 1;
        config.entries.addAll(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", "paladins:village/desert/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", "paladins:village/savanna/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", "paladins:village/plains/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", "paladins:village/taiga/sanctuary", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", "paladins:village/snowy/sanctuary", weight, limit)
        ));
        return config;
    }
}
