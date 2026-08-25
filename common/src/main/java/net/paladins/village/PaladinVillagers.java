package net.paladins.village;

import com.google.common.collect.ImmutableSet;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.item.PaladinWeapons;
import net.paladins.item.armor.Armors;
import net.paladins.content.PaladinSounds;
import net.runes.api.RuneItems;
import net.spell_engine.Platform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PaladinVillagers {
    public static final String PALADIN_MERCHANT = "monk";
    public static final Identifier POI_ID = Identifier.of(PaladinsMod.ID, PALADIN_MERCHANT);
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;

    /// The monk-workbench workstation block states for the POI. Registration itself is loader-specific
    /// (Fabric: `PointOfInterestHelper`; NeoForge: a plain `Registry.register` of a `PointOfInterestType`,
    /// whose block-state mapping NeoForge wires via its POI registry callback) — done in each platform's
    /// entrypoint; this only exposes the shared state set.
    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(PaladinBlocks.MONK_WORKBENCH.getStateManager().getStates());
    }

    /// The registered monk profession, set by {@link #registerVillagers()}. Read by the loader-specific
    /// trade-offer registration (Fabric `TradeOfferHelper` / NeoForge `VillagerTradesEvent`).
    public static VillagerProfession PROFESSION;

    /// The profession's registry key. Both loaders now address professions by key
    /// (Fabric `TradeOfferHelper.registerVillagerOffers(RegistryKey, ...)`,
    /// NeoForge `VillagerTradesEvent#getType()`), so it is kept alongside the value.
    public static final RegistryKey<VillagerProfession> PROFESSION_KEY =
            RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, Identifier.of(PaladinsMod.ID, PALADIN_MERCHANT));

    /// Trade offers per merchant tier (1..5), populated by {@link #registerVillagers()}. Actual registration
    /// with the game is loader-specific and lives in each platform's entrypoint.
    public static final LinkedHashMap<Integer, List<TradeOffers.Factory>> TRADES = new LinkedHashMap<>();

    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = Identifier.of(PaladinsMod.ID, name);
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(PaladinsMod.ID, name), new VillagerProfession(
                // 1.21.11: `VillagerProfession.id` is a Text (the profession's display name)
                Text.translatable("entity.minecraft.villager." + id.getNamespace() + "." + id.getPath()),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                PaladinSounds.paladin_armor_equip.soundEvent())
        );
    }

//    private static class Offer {
//        int level;
//        ItemStack input;
//        ItemStack output;
//        int maxUses;
//        int experience;
//        float priceMultiplier;
//
//        public Offer(int level, ItemStack input, ItemStack output, int maxUses, int experience, float priceMultiplier) {
//            this.level = level;
//            this.input = input;
//            this.output = output;
//            this.maxUses = maxUses;
//            this.experience = experience;
//            this.priceMultiplier = priceMultiplier;
//        }
//
//        public static Offer buy(int level, ItemStack item, int price, int maxUses, int experience, float priceMultiplier) {
//            return new Offer(level, item, new ItemStack(Items.EMERALD, price), maxUses, experience, priceMultiplier);
//        }
//
//        public static Offer sell(int level, ItemStack item, int price, int maxUses, int experience, float priceMultiplier) {
//            return new Offer(level, new ItemStack(Items.EMERALD, price), item, maxUses, experience, priceMultiplier);
//        }
//    }

    public static void registerVillagers() {
        if (!Platform.util().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(PaladinsMod.villageConfig.value);
        }
        PROFESSION = registerProfession(
                PALADIN_MERCHANT,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), POI_ID));

//        List<Offer> paladinMerchantOffers = List.of(
//                Offer.sell(1, new ItemStack(RuneItems.get(RuneItems.RuneType.HEALING), 8), 2, 128, 1, 0.01f),
//                Offer.sell(1, Weapons.acolyte_wand.item().getDefaultStack(), 4, 12, 5, 0.1f),
//                Offer.sell(1, Weapons.wooden_great_hammer.item().getDefaultStack(), 8, 12, 8, 0.1f),
//                Offer.buy(2, new ItemStack(Items.WHITE_WOOL, 5), 8, 12, 8, 0.0f),
//                Offer.buy(2, new ItemStack(Items.IRON_INGOT, 6), 9, 12, 8, 0.0f),
//                Offer.buy(2, new ItemStack(Items.IRON_CHAIN, 6), 3, 12, 8, 0.0f),
//                Offer.buy(2, new ItemStack(Items.GOLD_INGOT, 6), 9, 12, 8, 0.0f),
//                Offer.sell(2, Weapons.holy_staff.item().getDefaultStack(), 12, 12, 10, 0.05f),
//                Offer.sell(2, Weapons.iron_great_hammer.item().getDefaultStack(), 12, 12, 10, 0.05f),
//                Offer.sell(3, Armors.paladinArmorSet_t1.head.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(3, Armors.paladinArmorSet_t1.feet.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(3, Armors.priestArmorSet_t1.head.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(3, Armors.priestArmorSet_t1.feet.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(4, Armors.paladinArmorSet_t1.chest.getDefaultStack(), 20, 12, 15, 0.05f),
//                Offer.sell(4, Armors.paladinArmorSet_t1.legs.getDefaultStack(), 20, 12, 15, 0.05f),
//                Offer.sell(4, Armors.priestArmorSet_t1.chest.getDefaultStack(), 20, 12, 15, 0.05f),
//                Offer.sell(4, Armors.priestArmorSet_t1.legs.getDefaultStack(), 20, 12, 15, 0.05f)
//            );

        TRADES.clear();
        TRADES.put(1, List.of(
                new TradeOffers.SellItemFactory(RuneItems.get(RuneItems.RuneType.HEALING), 2, 8, 128, 1, 0.01f),
                new TradeOffers.SellItemFactory(PaladinWeapons.acolyte_wand.item(), 4, 1, 12, 5),
                new TradeOffers.SellItemFactory(PaladinWeapons.wooden_great_hammer.item(), 8, 1, 12, 8)
        ));
        TRADES.put(2, List.of(
                new TradeOffers.BuyItemFactory(Items.WHITE_WOOL, 5, 12, 5, 8),
                new TradeOffers.BuyItemFactory(Items.IRON_INGOT, 6, 12, 5, 8),
                new TradeOffers.BuyItemFactory(Items.IRON_CHAIN, 6, 12, 5, 8),
                new TradeOffers.BuyItemFactory(Items.GOLD_INGOT, 6, 12, 5, 8)
        ));
        TRADES.put(3, List.of(
                new TradeOffers.SellItemFactory(Armors.paladinArmorSet_t1.head, 15, 1, 12, 13),
                new TradeOffers.SellItemFactory(Armors.paladinArmorSet_t1.feet, 15, 1, 12, 13),
                new TradeOffers.SellItemFactory(Armors.priestArmorSet_t1.head, 15, 1, 12, 13),
                new TradeOffers.SellItemFactory(Armors.priestArmorSet_t1.feet, 15, 1, 12, 13)
        ));
        TRADES.put(4, List.of(
                new TradeOffers.SellItemFactory(Armors.paladinArmorSet_t1.chest, 20, 1, 12, 15),
                new TradeOffers.SellItemFactory(Armors.paladinArmorSet_t1.legs, 20, 1, 12, 15),
                new TradeOffers.SellItemFactory(Armors.priestArmorSet_t1.chest, 20, 1, 12, 15),
                new TradeOffers.SellItemFactory(Armors.priestArmorSet_t1.legs, 20, 1, 12, 15)
        ));
        TRADES.put(5, List.of(
                (TradeOffers.Factory) (world, entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        PaladinWeapons.diamond_holy_staff.item(), 40, 3, 30, 0F).create(world, entity, random),
                (TradeOffers.Factory) (world, entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        PaladinWeapons.diamond_claymore.item(), 40, 3, 30, 0F).create(world, entity, random),
                (TradeOffers.Factory) (world, entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        PaladinWeapons.diamond_great_hammer.item(), 40, 3, 30, 0F).create(world, entity, random)
        ));
    }
}
