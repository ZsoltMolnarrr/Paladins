package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
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

        // Villager POI + trades — Fabric API registration (loader-specific; NeoForge does its own).
        PointOfInterestHelper.register(PaladinVillagers.POI_ID,
                PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE,
                PaladinVillagers.poiBlockStates());
        PaladinsMod.registerVillagers(); // registers the profession + builds PaladinVillagers.TRADES
        PaladinVillagers.TRADES.forEach((tier, factories) ->
                TradeOfferHelper.registerVillagerOffers(PaladinVillagers.PROFESSION_KEY, tier,
                        list -> list.addAll(factories)));

        // Monk workbench into the Paladins creative tab — Fabric API.
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content ->
                content.add(PaladinBlocks.MONK_WORKBENCH_BLOCK));
    }
}
