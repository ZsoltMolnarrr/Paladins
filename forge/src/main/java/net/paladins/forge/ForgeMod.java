package net.paladins.forge;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegisterEvent;
import net.paladins.PaladinsMod;
import net.paladins.forge.client.ForgeClientMod;
import net.paladins.village.PaladinVillagers;

@Mod(PaladinsMod.ID)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        // Run our common setup (configs only — registers nothing).
        PaladinsMod.init();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Villager trades — game-bus event (fired per profession); replaces Fabric API's TradeOfferHelper.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, VillagerTradesEvent.class,
                ForgeMod::onVillagerTrades);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    /// Forge 47 unfreezes exactly one registry per `RegisterEvent` window, so registration is split by
    /// registry. The creative-tab *contents* are dispatched by SpellEngine's `PlatformEvents.onItemGroupModify`
    /// (called from `Armor.Set#register` / `Weapon.register` / `Shield.register`); the group itself is a
    /// vanilla-only registry that stays unfrozen for the whole phase, so registering it here is fine.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> PaladinsMod.registerSounds());
        event.register(RegistryKeys.BLOCK, reg -> PaladinsMod.registerBlocks());
        event.register(RegistryKeys.STATUS_EFFECT, reg -> PaladinsMod.registerEffects());
        event.register(RegistryKeys.ENTITY_TYPE, reg -> PaladinsMod.registerEntities());
        event.register(RegistryKeys.ITEM, reg -> PaladinsMod.registerItems());
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // POI registration — a plain vanilla registry insert. Forge 47's PointOfInterestTypeCallbacks
            // fills the blockstate -> POI map from the type's own states, so no helper is needed.
            Registry.register(Registries.POINT_OF_INTEREST_TYPE, PaladinVillagers.POI_ID,
                    new PointOfInterestType(PaladinVillagers.poiBlockStates(),
                            PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE));
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            PaladinsMod.registerVillagers(); // registers the profession + builds PaladinVillagers.TRADES
        });
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != PaladinVillagers.PROFESSION) {
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
