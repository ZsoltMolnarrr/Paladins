package net.paladins.village;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.item.PaladinWeapons;
import net.paladins.item.armor.Armors;
import net.paladins.content.PaladinSounds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PaladinVillagers {
    public static final String PALADIN_MERCHANT = "monk";
    public static final Identifier POI_ID = new Identifier(PaladinsMod.ID, PALADIN_MERCHANT);
    /// The villager profession's own id. Same path as {@link #POI_ID}, but a different registry.
    public static final Identifier PROFESSION_ID = new Identifier(PaladinsMod.ID, PALADIN_MERCHANT);
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

    /// Trade offers per merchant tier (1..5), populated by {@link #registerVillagers()}. Actual registration
    /// with the game is loader-specific and lives in each platform's entrypoint.
    public static final LinkedHashMap<Integer, List<TradeOffers.Factory>> TRADES = new LinkedHashMap<>();

    public static VillagerProfession createProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = new Identifier(PaladinsMod.ID, name);
        return new VillagerProfession(
                id.toString(),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                PaladinSounds.paladin_armor_equip.soundEvent()
        );
    }

    private static VillagerProfession monkProfession;

    /// Builds the monk profession once, keyed by {@link #PROFESSION_ID}. Creation only — nothing is
    /// registered here, so a loader that registers the profession itself hands this to its own
    /// registration API instead of duplicating the construction.
    public static VillagerProfession professionToRegister() {
        if (monkProfession == null) {
            monkProfession = createProfession(
                    PALADIN_MERCHANT,
                    RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), POI_ID));
        }
        return monkProfession;
    }

    /// Reads {@link #PROFESSION} back out of the registry, for a loader that registered the profession
    /// itself (Forge's `RegisterEvent` helper returns void where `Registry.register` returns the value).
    /// `VillagerTradesEvent` filters on this field, so it has to be set on both loaders. Throws if the
    /// profession is missing — which is also what catches a silently mis-keyed `event.register` block.
    public static void linkProfessionEntry() {
        if (PROFESSION == null) {
            PROFESSION = Registries.VILLAGER_PROFESSION
                    .getOrEmpty(PROFESSION_ID)
                    .orElseThrow(() -> new IllegalStateException(
                            "Villager profession " + PROFESSION_ID + " is not in the registry — register it first"));
        }
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
        PROFESSION = Registry.register(Registries.VILLAGER_PROFESSION, PROFESSION_ID, professionToRegister());
        buildTrades();
    }

    /// Populates {@link #TRADES}. Creation only — actual registration with the game is loader-specific
    /// (Fabric `TradeOfferHelper` / Forge `VillagerTradesEvent`) and lives in each platform's entrypoint.
    /// Reads the ITEM registry through the weapon and armor entries, so it must run after items register
    /// (`item` is Forge event 7, `villager_profession` event 28).
    public static void buildTrades() {

//        List<Offer> paladinMerchantOffers = List.of(
//                Offer.sell(1, new ItemStack(RuneItems.get(RuneItems.RuneType.HEALING), 8), 2, 128, 1, 0.01f),
//                Offer.sell(1, Weapons.acolyte_wand.item().getDefaultStack(), 4, 12, 5, 0.1f),
//                Offer.sell(1, Weapons.wooden_great_hammer.item().getDefaultStack(), 8, 12, 8, 0.1f),
//                Offer.buy(2, new ItemStack(Items.WHITE_WOOL, 5), 8, 12, 8, 0.0f),
//                Offer.buy(2, new ItemStack(Items.IRON_INGOT, 6), 9, 12, 8, 0.0f),
//                Offer.buy(2, new ItemStack(Items.CHAIN, 6), 3, 12, 8, 0.0f),
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
                // Runes is optional on this line (no Forge artifact on 1.20.1) and is not on the compile
                // classpath. Resolve the healing stone by registry id at offer-creation time; a null offer
                // is how vanilla says "no trade", so the tier simply loses this entry when Runes is absent.
                sellIfPresent("runes:healing_stone", 8, 2, 128, 1, 0.01f),
                sell(PaladinWeapons.acolyte_wand.item(), 4, 1, 12, 5),
                sell(PaladinWeapons.wooden_great_hammer.item(), 8, 1, 12, 8)
        ));
        TRADES.put(2, List.of(
                buy(Items.WHITE_WOOL, 5, 12, 5, 8),
                buy(Items.IRON_INGOT, 6, 12, 5, 8),
                buy(Items.CHAIN, 6, 12, 5, 8),
                buy(Items.GOLD_INGOT, 6, 12, 5, 8)
        ));
        TRADES.put(3, List.of(
                sell(Armors.paladinArmorSet_t1.head, 15, 1, 12, 13),
                sell(Armors.paladinArmorSet_t1.feet, 15, 1, 12, 13),
                sell(Armors.priestArmorSet_t1.head, 15, 1, 12, 13),
                sell(Armors.priestArmorSet_t1.feet, 15, 1, 12, 13)
        ));
        TRADES.put(4, List.of(
                sell(Armors.paladinArmorSet_t1.chest, 20, 1, 12, 15),
                sell(Armors.paladinArmorSet_t1.legs, 20, 1, 12, 15),
                sell(Armors.priestArmorSet_t1.chest, 20, 1, 12, 15),
                sell(Armors.priestArmorSet_t1.legs, 20, 1, 12, 15)
        ));
        TRADES.put(5, List.of(
                sellEnchanted(PaladinWeapons.diamond_holy_staff.item(), 40, 3, 30, 0F),
                sellEnchanted(PaladinWeapons.diamond_claymore.item(), 40, 3, 30, 0F),
                sellEnchanted(PaladinWeapons.diamond_great_hammer.item(), 40, 3, 30, 0F)
        ));
    }

    // MARK: Trade factories
    //
    // 1.20.1's `TradeOffers` has no `BuyItemFactory` at all, and `SellItemFactory` / `SellEnchantedToolFactory`
    // — public in the decompiled tree — are *package-private* classes in the real 1.20.1 jar, and stay so
    // even after Forge's access transformer. This module compiles against them only because another
    // dependency on `common`'s classpath contributes an access widener that the production runtime lacks;
    // on a Forge server that is an `IllegalAccessError: VillagerTrades$ItemsForEmeralds` at trade-build time.
    // All three are therefore rebuilt on the raw `TradeOffer` constructor, reproducing vanilla's arithmetic
    // exactly (including its 0.05 default price multiplier).

    /// Mirrors vanilla's buy offer: `count` of `item` for `price` emeralds.
    private static TradeOffers.Factory buy(ItemConvertible item, int count, int maxUses, int experience, int price) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), new ItemStack(Items.EMERALD, price), maxUses, experience, 0.05F);
    }

    /// Mirrors `TradeOffers.SellItemFactory(Item, price, count, maxUses, experience)`.
    private static TradeOffers.Factory sell(ItemConvertible item, int price, int count, int maxUses, int experience) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, price), new ItemStack(item, count), maxUses, experience, 0.05F);
    }

    /// Mirrors `TradeOffers.SellEnchantedToolFactory(Item, basePrice, maxUses, experience, multiplier)`:
    /// a random enchantment level in `[5, 20)`, no treasure enchantments, and — the rule that is easy to
    /// drop — **that level is added to the price**, capped at a stack of 64 emeralds.
    private static TradeOffers.Factory sellEnchanted(net.minecraft.item.Item item, int basePrice,
                                                     int maxUses, int experience, float multiplier) {
        return (entity, random) -> {
            int level = 5 + random.nextInt(15);
            var enchanted = EnchantmentHelper.enchant(random, new ItemStack(item), level, false);
            int price = Math.min(basePrice + level, 64);
            return new TradeOffer(new ItemStack(Items.EMERALD, price), enchanted, maxUses, experience, multiplier);
        };
    }

    /// Sells `count` of the item registered under `itemId` for `price` emeralds — resolved lazily, at
    /// offer-creation time, so an optional mod's item can be missing without any init-order requirement.
    /// Returning null is how vanilla expresses "no offer".
    private static TradeOffers.Factory sellIfPresent(String itemId, int count, int price,
                                                     int maxUses, int xp, float multiplier) {
        var id = new Identifier(itemId);
        return (entity, random) -> {
            var item = Registries.ITEM.get(id);
            if (item == null || item == Items.AIR) {
                return null;
            }
            return new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(item, count),
                    maxUses, xp, multiplier);
        };
    }
}
