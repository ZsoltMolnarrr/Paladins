package net.paladins;

import net.minecraft.item.ItemGroup;
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
import net.spell_engine.Platform;
import net.spell_engine.PlatformEvents;
import net.spell_engine.rpg_series.config.ConfigFile;
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

    // `villages.json` moved to `net.paladins.fabric.village.FabricVillageStructures`: StructurePoolAPI
    // is Fabric-only on 1.20.1 and its config type may not be referenced from `common`.

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
        tweaksConfig.refresh();
        if (Platform.util().isDevelopmentEnvironment()) {
            tweaksConfig.value.ignore_items_required_mods = true;
        }
    }

    public static void registerSounds() {
        PaladinSounds.register();
    }

    public static void registerBlocks() {
        PaladinBlocks.register();
    }

    /// The monk workbench's `BlockItem` — registered from the ITEM window on Forge, one registry per
    /// `RegisterEvent` window.
    public static void registerBlockItems() {
        PaladinBlocks.registerBlockItems();
    }

    public static void registerItems() {
        // `ItemGroup.Builder` is a vanilla type on this line (the 1.21 `ItemGroup.builder()` static is a
        // Fabric API interface injection); the ITEM_GROUP registry is vanilla-only and stays unfrozen for
        // the whole Forge RegisterEvent phase, so registering it from the ITEM window is fine.
        Group.PALADINS = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                .displayName(Text.translatable("itemGroup.paladins.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.PALADINS);
        registerBlockItems();
        PaladinBooks.register();

        PaladinWeapons.register(itemConfig.value.weapons);
        PaladinShields.register(shieldConfig.value.shields);
        Armors.register(itemConfig.value.armor_sets);
        shieldConfig.save();
        itemConfig.save();

        // The monk workbench block item into the Paladins creative tab. Dispatched by SpellEngine on both
        // loaders (Fabric `ItemGroupEvents` / Forge `BuildCreativeModeTabContentsEvent`).
        PlatformEvents.onItemGroupModify(Group.KEY, (content, context) ->
                content.add(PaladinBlocks.MONK_WORKBENCH_BLOCK));
    }

    /// Entity types live in their own registry: on Forge 47 exactly one registry is unfrozen per
    /// `RegisterEvent` window, so this must NOT run inside the ITEM window.
    public static void registerEntities() {
        PaladinEntities.register();
    }

    public static void registerEffects() {
        PaladinEffects.register(effectsConfig.value);
        effectsConfig.save();
    }

    public static void registerVillagers() {
        PaladinVillagers.registerVillagers();
    }
}