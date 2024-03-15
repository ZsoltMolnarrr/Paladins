package net.paladins;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.paladins.block.PaladinBlocks;
import net.paladins.config.Default;
import net.paladins.config.EffectsConfig;
import net.paladins.effect.Effects;
import net.paladins.entity.BannerEntity;
import net.paladins.entity.BarrierEntity;
import net.paladins.item.Group;
import net.paladins.item.PaladinBooks;
import net.paladins.item.PaladinItems;
import net.paladins.item.Weapons;
import net.paladins.item.armor.Armors;
import net.paladins.util.SoundHelper;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.loot.LootConfig;
import net.spell_engine.api.loot.LootHelper;
import net.tinyconfig.ConfigManager;

public class PaladinsMod implements ModInitializer {
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

    public static ConfigManager<EffectsConfig> effectsConfig = new ConfigManager<EffectsConfig>
            ("effects", new EffectsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public void onInitialize() {
        lootConfig.refresh();
        itemConfig.refresh();
        effectsConfig.refresh();
        Group.PALADINS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                .displayName(Text.translatable("itemGroup.paladins.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.PALADINS);
        PaladinBlocks.register();
        PaladinBooks.register();
        Weapons.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
        villageConfig.refresh();
        Effects.register();
        PaladinVillagers.register();

        SoundHelper.registerSounds();
        subscribeEvents();
    }

    private void subscribeEvents() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootHelper.configure(id, tableBuilder, PaladinsMod.lootConfig.value, PaladinItems.entries);
        });
    }

    static {
        BarrierEntity.TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(ID, "barrier"),
                FabricEntityTypeBuilder.<BarrierEntity>create(SpawnGroup.MISC, BarrierEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
        BannerEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(ID, "battle_banner"),
                FabricEntityTypeBuilder.<BannerEntity>create(SpawnGroup.MISC, BannerEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F)) // dimensions in Minecraft units of the render
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
    }
}