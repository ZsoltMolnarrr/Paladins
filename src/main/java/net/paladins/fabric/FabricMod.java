package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.paladins.PaladinsMod;
import net.paladins.item.Group;
import net.paladins.item.PaladinItems;
import net.paladins.item.armor.Armors;
import net.paladins.util.SoundHelper;
import net.spell_engine.api.loot.LootHelper;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        preInit();
        PaladinsMod.init();
        SoundHelper.registerSounds();
        subscribeEvents();
    }

    private void preInit() {
        Group.PALADINS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.paladinArmorSet_t2.head))
                .displayName(Text.translatable("itemGroup.paladins.general"))
                .build();
    }

    private void subscribeEvents() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootHelper.configure(id, tableBuilder, PaladinsMod.lootConfig.value, PaladinItems.entries);
        });
    }

}