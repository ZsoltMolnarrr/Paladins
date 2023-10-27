package net.paladins;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.paladins.block.PaladinBlocks;
import net.paladins.config.Default;
import net.paladins.effect.Effects;
import net.paladins.item.Group;
import net.paladins.item.PaladinBooks;
import net.paladins.item.armor.Armors;
import net.paladins.item.Weapons;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.loot.LootConfig;
import net.tinyconfig.ConfigManager;

public class PaladinsMod {
    public static final String ID = "paladins";

    public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<>
            ("items_v3", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();
    public static ConfigManager<LootConfig> lootConfig = new ConfigManager<>
            ("loot_v2", Default.lootConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<>
            ("villages", Default.villageConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static void init() {
        lootConfig.refresh();
        itemConfig.refresh();
        PaladinBooks.register();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.PALADINS);
        Weapons.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
        villageConfig.refresh();
        Effects.register();
        PaladinBlocks.register();
        PaladinVillagers.register();
    }
}