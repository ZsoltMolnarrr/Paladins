package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.item.Group;
import net.paladins.village.PaladinVillagers;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PaladinsMod.init();
        PaladinsMod.registerSounds();
        PaladinsMod.registerBlocks();
        PaladinsMod.registerItems();
        PaladinsMod.registerEffects();

        // Villager POI — Fabric API registration (loader-specific; NeoForge does its own).
        // Trades are data-driven since 26.1 (`data/paladins/{villager_trade,trade_set}/monk/**`),
        // so there is nothing to register here any more (`TradeOfferHelper` is gone).
        PoiHelper.register(PaladinVillagers.POI_ID,
                PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE,
                PaladinVillagers.poiBlockStates());
        PaladinsMod.registerVillagers();

        // Monk workbench into the Paladins creative tab — Fabric API.
        CreativeModeTabEvents.modifyOutputEvent(Group.KEY).register(content ->
                content.accept(PaladinBlocks.MONK_WORKBENCH_BLOCK));
    }
}
