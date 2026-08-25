package net.paladins.neoforge;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.item.Group;
import net.paladins.village.PaladinVillagers;

@Mod(PaladinsMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        PaladinsMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Monk workbench into the Paladins creative tab — NeoForge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
        // Villager trades — game-bus event (fired per profession); replaces Fabric API's TradeOfferHelper.
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, NeoForgeMod::onVillagerTrades);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            PaladinsMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            PaladinsMod.registerItems();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            PaladinsMod.registerBlocks();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            PaladinsMod.registerEffects();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // POI registration — vanilla registry insert. NeoForge's POI registry callback wires the
            // block-state -> POI mapping from the type's block states, so no Fabric API helper is needed.
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                Registry.register(Registries.POINT_OF_INTEREST_TYPE, PaladinVillagers.POI_ID,
                        new PointOfInterestType(PaladinVillagers.poiBlockStates(),
                                PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE));
            } catch (Exception e) { }
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            PaladinsMod.registerVillagers(); // registers the profession + builds PaladinVillagers.TRADES
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(Group.KEY)) {
            event.add(PaladinBlocks.MONK_WORKBENCH_BLOCK);
        }
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (!PaladinVillagers.PROFESSION_KEY.equals(event.getType())) {
            return;
        }
        PaladinVillagers.TRADES.forEach((tier, factories) -> {
            var tierList = event.getTrades().get(tier.intValue());
            if (tierList != null) {
                tierList.addAll(factories);
            }
        });
    }
}
