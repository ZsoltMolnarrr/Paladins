package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.paladins.PaladinsMod;
import net.paladins.fabric.village.FabricVillageStructures;
import net.paladins.item.PaladinShields;
import net.paladins.village.PaladinVillagers;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PaladinsMod.init();
        // Install the shield item factory before the items register (mirrors ForgeMod).
        PaladinShields.factory = CustomShieldItem::new;
        // StructurePoolAPI is Fabric-only on 1.20.1 — install the village injector before the villagers
        // are registered (PaladinVillagers.registerVillagers() calls it).
        FabricVillageStructures.install();
        PaladinsMod.registerSounds();
        PaladinsMod.registerBlocks();
        PaladinsMod.registerEntities();
        PaladinsMod.registerItems();
        PaladinsMod.registerEffects();

        // Villager POI + trades — Fabric API registration (loader-specific; Forge does its own).
        PointOfInterestHelper.register(PaladinVillagers.POI_ID,
                PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE,
                PaladinVillagers.poiBlockStates());
        PaladinsMod.registerVillagers(); // registers the profession + builds PaladinVillagers.TRADES
        PaladinVillagers.TRADES.forEach((tier, factories) ->
                TradeOfferHelper.registerVillagerOffers(PaladinVillagers.PROFESSION, tier,
                        list -> list.addAll(factories)));
    }
}
