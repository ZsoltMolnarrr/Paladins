package net.paladins;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.Platform;
import net.spell_engine.PlatformEvents;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.tiny_config.ConfigManager;

import java.util.LinkedHashMap;
import java.util.Map;

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
        tweaksConfig.refresh();
        villageConfig.refresh();
        if (!Platform.util().isModLoaded("lithostitched")) {
            // Only inject the sanctuary if Lithostitched is not present - otherwise the data-driven
            // paths in `resources/data/paladins` already do it.
            //
            // `injectAll` only *queues* the entries; StructurePoolAPI's own entrypoint applies them
            // when the server starts (Fabric SERVER_STARTING / Forge ServerAboutToStartEvent, both
            // before the spawn region generates). The queue is deliberately never cleared, so this
            // must be called exactly once, here at mod init - never per world load.
            StructurePoolAPI.injectAll(villageConfig.value);
        }
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

    public static void registerItems() {
        registerItemGroup();
        itemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
    }

    /// Builds the `paladins:generic` creative tab. Creation only — nothing is registered here, so a loader
    /// that registers item groups itself (Forge) hands this to its own helper. Built once.
    ///
    /// `ItemGroup.Builder` is a vanilla type on this line (the 1.21 `ItemGroup.builder()` static is a
    /// Fabric API interface injection). The icon is a lazy supplier, so this does not depend on the armor
    /// items already existing.
    public static ItemGroup createItemGroup() {
        if (Group.PALADINS == null) {
            Group.PALADINS = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                    .displayName(Text.translatable("itemGroup.paladins.general"))
                    .build();
        }
        return Group.PALADINS;
    }

    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, Group.KEY, createItemGroup());
    }

    /// Every item Paladins adds, keyed by the id it registers under. Creation only — nothing is written
    /// into the ITEM registry here, so a loader that registers items itself (Forge) iterates this instead
    /// of calling {@link #registerItems()}. **Must run inside the ITEM registration window**: `Item`'s
    /// constructor creates an intrusive registry holder.
    ///
    /// Also installs the creative-tab contents callbacks — the group *contents*, not the group itself,
    /// which is a separate registry (`creative_mode_tab` is Forge event 65, `item` is event 7).
    public static Map<Identifier, Item> itemsToRegister() {
        var items = new LinkedHashMap<Identifier, Item>();
        items.putAll(blockItemsToRegister());

        // The monk workbench block item into the Paladins creative tab. Dispatched by SpellEngine on both
        // loaders (Fabric `ItemGroupEvents` / Forge `BuildCreativeModeTabContentsEvent`).
        //
        // ORDER MATTERS: on both loaders the group modifiers run in *registration* order, so this listener
        // is installed BEFORE the book/weapon/shield/armor registrations install SpellEngine's own listeners
        // — that is what puts the block at the front of the tab.
        PlatformEvents.onItemGroupModify(Group.KEY, (content, context) ->
                content.add(PaladinBlocks.MONK_WORKBENCH_BLOCK));

        PaladinBooks.register();

        items.putAll(PaladinWeapons.itemsToRegister(itemConfig.value.weapons));
        items.putAll(PaladinShields.itemsToRegister(shieldConfig.value.shields));
        items.putAll(Armors.itemsToRegister(itemConfig.value.armor_sets));
        shieldConfig.save();
        itemConfig.save();
        return items;
    }

    /// The monk workbench's `BlockItem`, keyed by its registration id. Creation only.
    public static Map<Identifier, Item> blockItemsToRegister() {
        return Map.of(PaladinBlocks.MONK_WORKBENCH_ID, PaladinBlocks.MONK_WORKBENCH_BLOCK);
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