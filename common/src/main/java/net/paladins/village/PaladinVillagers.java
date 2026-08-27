package net.paladins.village;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.rpg_foundation.structure_pool.api.StructurePoolAPI;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.state.BlockState;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.content.PaladinSounds;
import net.spell_engine.Platform;

import java.util.Set;

public class PaladinVillagers {
    public static final String PALADIN_MERCHANT = "monk";
    public static final Identifier POI_ID = Identifier.fromNamespaceAndPath(PaladinsMod.ID, PALADIN_MERCHANT);
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;

    /// The monk-workbench workstation block states for the POI. Registration itself is loader-specific
    /// (Fabric: `PoiHelper`; NeoForge: a plain `Registry.register` of a `PointOfInterestType`,
    /// whose block-state mapping NeoForge wires via its POI registry callback) — done in each platform's
    /// entrypoint; this only exposes the shared state set.
    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(PaladinBlocks.MONK_WORKBENCH.getStateDefinition().getPossibleStates());
    }

    /// The registered monk profession, set by {@link #registerVillagers()}.
    public static VillagerProfession PROFESSION;

    /// The profession's registry key.
    public static final ResourceKey<VillagerProfession> PROFESSION_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(PaladinsMod.ID, PALADIN_MERCHANT));

    /// Merchant tiers the monk offers trades on (1..5).
    public static final int MAX_MERCHANT_LEVEL = 5;

    /// 26.1 made villager trades data-driven: the `VillagerTrades` static maps, Fabric's
    /// `TradeOfferHelper` and NeoForge's `VillagerTradesEvent` are all gone. A profession now only
    /// names one `TradeSet` per merchant level; the sets, the individual trades and the trade tags
    /// live in `data/paladins/{trade_set,villager_trade,tags/villager_trade}/monk/**`.
    public static ResourceKey<TradeSet> tradeSet(int level) {
        return ResourceKey.create(Registries.TRADE_SET,
                Identifier.fromNamespaceAndPath(PaladinsMod.ID, PALADIN_MERCHANT + "/level_" + level));
    }

    private static Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel() {
        return Int2ObjectMap.ofEntries(
                Int2ObjectMap.entry(1, tradeSet(1)),
                Int2ObjectMap.entry(2, tradeSet(2)),
                Int2ObjectMap.entry(3, tradeSet(3)),
                Int2ObjectMap.entry(4, tradeSet(4)),
                Int2ObjectMap.entry(5, tradeSet(5))
        );
    }

    public static VillagerProfession registerProfession(String name, ResourceKey<PoiType> workStation) {
        var id = Identifier.fromNamespaceAndPath(PaladinsMod.ID, name);
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, id, new VillagerProfession(
                // The display name is ours to pick; keep the pre-26.1 key so the shipped translations
                // (vanilla's own convention moved to `entity.<namespace>.villager.<path>`) keep working.
                Component.translatable("entity.minecraft.villager." + id.getNamespace() + "." + id.getPath()),
                (entry) -> entry.is(workStation),
                (entry) -> entry.is(workStation),
                ImmutableSet.of(),
                ImmutableSet.of(),
                PaladinSounds.paladin_armor_equip.soundEvent(),
                tradeSetsByLevel())
        );
    }

    public static void registerVillagers() {
        if (!Platform.util().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(PaladinsMod.villageConfig.value);
        }
        PROFESSION = registerProfession(
                PALADIN_MERCHANT,
                ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), POI_ID));
    }
}
