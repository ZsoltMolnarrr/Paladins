package net.paladins;

import net.spell_engine.api.item.ItemConfig;
import net.tinyconfig.ConfigManager;
import net.paladins.config.Default;
import net.paladins.config.LootConfig;
import net.paladins.config.WorldGenConfig;
import net.paladins.effect.Effects;
import net.paladins.item.Armors;
import net.paladins.item.Weapons;
import net.paladins.villager.PaladinVillagers;

public class PaladinsMod {
    public static final String ID = "paladins";

    public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
            ("items", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();
    public static ConfigManager<LootConfig> lootConfig = new ConfigManager<LootConfig>
            ("loot", Default.lootConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<WorldGenConfig> worldGenConfig = new ConfigManager<WorldGenConfig>
            ("world_gen", Default.worldGenConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static void init() {
        lootConfig.refresh();
        itemConfig.refresh();
        Weapons.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
        worldGenConfig.refresh();
        Effects.register();
        PaladinVillagers.register();
    }
}