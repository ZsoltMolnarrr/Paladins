package net.paladins.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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
        // Villager trades are data-driven since 26.1 (`data/paladins/{villager_trade,trade_set}/monk/**`);
        // `VillagerTradesEvent` no longer exists, so there is nothing to hook here.
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, reg -> {
            PaladinsMod.registerSounds();
        });
        event.register(Registries.ITEM, reg -> {
            PaladinsMod.registerItems();
        });
        event.register(Registries.BLOCK, reg -> {
            PaladinsMod.registerBlocks();
        });
        event.register(Registries.MOB_EFFECT, reg -> {
            PaladinsMod.registerEffects();
        });
        event.register(Registries.POINT_OF_INTEREST_TYPE, reg -> {
            // POI registration — vanilla registry insert. NeoForge's POI registry callback wires the
            // block-state -> POI mapping from the type's block states, so no Fabric API helper is needed.
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, PaladinVillagers.POI_ID,
                        new PoiType(PaladinVillagers.poiBlockStates(),
                                PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE));
            } catch (Exception e) { }
        });
        event.register(Registries.VILLAGER_PROFESSION, reg -> {
            PaladinsMod.registerVillagers();
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(Group.KEY)) {
            event.accept(PaladinBlocks.MONK_WORKBENCH_BLOCK);
        }
    }
}
