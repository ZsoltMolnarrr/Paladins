package net.paladins.forge;

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
import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.content.PaladinSounds;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.PaladinEntities;
import net.paladins.forge.client.ForgeClientMod;
import net.paladins.item.Group;
import net.paladins.item.PaladinShields;
import net.paladins.village.PaladinVillagers;
import net.spell_engine.api.effect.Effects;

@Mod(PaladinsMod.ID)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        // Run our common setup (configs only — registers nothing).
        PaladinsMod.init();
        // Install the shield item factory before anything registers items (mirrors FabricMod).
        PaladinShields.factory = CustomShieldItem::new;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Villager trades — game-bus event (fired per profession); replaces Fabric API's TradeOfferHelper.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, VillagerTradesEvent.class,
                ForgeMod::onVillagerTrades);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    /// Registration is duplicated here rather than delegated to `common`'s `registerX()` methods, because a
    /// plain `Registry.register` is not usable on this loader: Forge only clears the vanilla registry's own
    /// lock from 47.4.0 onwards, so on 47.0–47.3 and NeoForge 1.20.1 it throws "Can not register to a locked
    /// registry" even inside the correct `RegisterEvent` window, and our `mods.toml` declares
    /// `loaderVersion = "[47,)"`. The helper this event hands out is the API every build of `[47,)`
    /// sanctions, so Forge iterates the same content `common` exposes through its `…ToRegister()` methods
    /// and registers it itself. `common` keeps its vanilla-shaped registration for Fabric, untouched.
    ///
    /// `event.register` is a no-op unless its key matches the event's registry, so all seven blocks are
    /// declared unconditionally; Forge posts one event per registry and each block runs in exactly its own.
    /// It also has no `else` and no throw, so a mis-keyed block loses its content **in silence** — hence
    /// the item group has its own block: `creative_mode_tab` is event 65, `item` is event 7.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper -> {
            PaladinSounds.soundsToRegister().forEach(helper::register);
            // The helper returns void, so the RegistryEntry fields read at class-init by the armor
            // materials (`Armors`) and the kite shields are filled in afterwards from the registry.
            PaladinSounds.linkEntries();
        });

        event.register(RegistryKeys.BLOCK, helper ->
                helper.register(PaladinBlocks.MONK_WORKBENCH_ID, PaladinBlocks.MONK_WORKBENCH));

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            PaladinEffects.configureEffects();
            Effects.effectsToRegister(PaladinEffects.entries, PaladinsMod.effectsConfig.value.effects)
                    .forEach(helper::register);
            // `Protection.register` reads `DIVINE_PROTECTION.entry`, which only the register-reference path
            // fills in — so linking has to happen before the behaviour wiring, not after.
            Effects.linkEntries(PaladinEffects.entries);
            PaladinEffects.installBehaviours();
            PaladinsMod.effectsConfig.save();
        });

        event.register(RegistryKeys.ENTITY_TYPE, helper -> {
            PaladinEntities.entityTypesToRegister().forEach(helper::register);
            PaladinEntities.attachSummonAttributes();
        });

        event.register(RegistryKeys.ITEM, helper ->
                PaladinsMod.itemsToRegister().forEach(helper::register));

        // `creative_mode_tab` is event 65, 58 events after `item` — its own window, or the write is dropped.
        event.register(RegistryKeys.ITEM_GROUP, helper ->
                helper.register(Group.KEY, PaladinsMod.createItemGroup()));

        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, helper -> {
            // Forge 47's PointOfInterestTypeCallbacks fills the blockstate -> POI map from the type's own
            // states as the entry is added, so nothing else is needed here.
            helper.register(PaladinVillagers.POI_ID,
                    new PointOfInterestType(PaladinVillagers.poiBlockStates(),
                            PaladinVillagers.POI_TICKET_COUNT, PaladinVillagers.POI_SEARCH_DISTANCE));
        });

        event.register(RegistryKeys.VILLAGER_PROFESSION, helper -> {
            helper.register(PaladinVillagers.PROFESSION_ID, PaladinVillagers.professionToRegister());
            // The helper returns void, so the field `VillagerTradesEvent` filters on is filled in afterwards.
            PaladinVillagers.linkProfessionEntry();
            PaladinVillagers.buildTrades();
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
