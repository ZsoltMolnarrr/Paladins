package net.paladins;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
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
import net.paladins.config.TweaksConfig;
import net.paladins.effect.Effects;
import net.paladins.entity.BannerEntity;
import net.paladins.entity.BarrierEntity;
import net.paladins.item.Group;
import net.paladins.item.PaladinBooks;
import net.paladins.item.Shields;
import net.paladins.item.Weapons;
import net.paladins.item.armor.Armors;
import net.paladins.util.SoundHelper;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.api.config.ConfigFile;
import net.tinyconfig.ConfigManager;

public class PaladinsMod implements ModInitializer {
    public static final String ID = "paladins";

    public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
            ("equipment", Default.itemConfig)
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

    public void onInitialize() {
        itemConfig.refresh();
        shieldConfig.refresh();
        tweaksConfig.refresh();
        villageConfig.refresh();
        SoundHelper.registerSounds();
        Group.PALADINS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                .displayName(Text.translatable("itemGroup.paladins.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.PALADINS);
        PaladinBlocks.register();
        PaladinBooks.register();
        Weapons.register(itemConfig.value.weapons);
        Shields.register(shieldConfig.value.shields);
        Armors.register(itemConfig.value.armor_sets);
        shieldConfig.save();
        itemConfig.save();
        Effects.register();
        PaladinVillagers.register();
        subscribeEvents();
    }

    private void subscribeEvents() {
    }

    static {
        BarrierEntity.TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(ID, "barrier"),
                FabricEntityTypeBuilder.<BarrierEntity>create(SpawnGroup.MISC, BarrierEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
        BannerEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(ID, "battle_banner"),
                FabricEntityTypeBuilder.<BannerEntity>create(SpawnGroup.MISC, BannerEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F)) // dimensions in Minecraft units of the render
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
    }
}