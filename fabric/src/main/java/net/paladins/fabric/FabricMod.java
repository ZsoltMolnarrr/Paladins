package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.Armors;
import net.paladins.item.Group;
import net.paladins.util.LootHelper;
import net.paladins.util.SoundHelper;
import net.paladins.worldgen.PaladinWorldGen;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        preInit();
        PaladinsMod.init();
//        SoundHelper.registerSounds();
//        subscribeEvents();
    }

    private void preInit() {
        Group.PALADINS = FabricItemGroupBuilder.build(
                new Identifier(PaladinsMod.ID, "general"),
                () -> new ItemStack(Armors.paladinArmorSet.head));
    }

    private void subscribeEvents() {
//        ServerLifecycleEvents.SERVER_STARTING.register(PaladinWorldGen::init);
//        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
//            LootHelper.configure(id, tableBuilder, PaladinsMod.lootConfig.value);
//        });
    }

}