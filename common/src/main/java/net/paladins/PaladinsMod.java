package net.paladins;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.paladins.block.PaladinBlocks;
import net.paladins.config.Default;
import net.paladins.config.TweaksConfig;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.PaladinEntities;
import net.paladins.item.Group;
import net.paladins.item.PaladinBooks;
import net.paladins.item.PaladinShields;
import net.paladins.item.PaladinWeapons;
import net.paladins.item.armor.Armors;
import net.paladins.content.PaladinSounds;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;

public class PaladinsMod {
    public static final String ID = "paladins";

    public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
            ("equipment_v2", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<ConfigFile.Shields> shieldConfig = new ConfigManager<>
            ("shields", new ConfigFile.Shields())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();
    public static ConfigManager<ConfigFile.Effects> effectsConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<>
            ("villages", Default.villageConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
            ("tweaks", new TweaksConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static void init() {
        itemConfig.refresh();
        shieldConfig.refresh();
        effectsConfig.refresh();
        villageConfig.refresh();
        tweaksConfig.refresh();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            tweaksConfig.value.ignore_items_required_mods = true;
        }
    }

    public static void registerSounds() {
        PaladinSounds.register();
    }

    public static void registerBlocks() {
        PaladinBlocks.register();
    }

    public static void registerItems() {
        Group.PALADINS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                .displayName(Text.translatable("itemGroup.paladins.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.PALADINS);
        PaladinBooks.register();

        PaladinWeapons.register(itemConfig.value.weapons);
        PaladinShields.register(shieldConfig.value.shields);
        Armors.register(itemConfig.value.armor_sets);
        shieldConfig.save();
        itemConfig.save();

        PaladinEntities.register();
    }

    public static void registerEffects() {
        PaladinEffects.register(effectsConfig.value);
        effectsConfig.save();
    }

    public static void registerPOI() {
        PaladinVillagers.registerPOI();
    }

    public static void registerVillagers() {
        PaladinVillagers.registerVillagers();
    }
}