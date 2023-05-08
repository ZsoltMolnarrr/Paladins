package net.paladins.config;

import net.spell_engine.api.item.ItemConfig;
import net.paladins.item.armor.Armors;
import net.paladins.item.Weapons;
import net.spell_engine.api.loot.LootConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ItemConfig itemConfig;
    public final static LootConfig lootConfig;
    public final static WorldGenConfig worldGenConfig;
    static {
        itemConfig = new ItemConfig();
        for (var weapon: Weapons.entries) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }
        for (var armorSet: Armors.entries) {
            itemConfig.armor_sets.put(armorSet.name(), armorSet.defaults());
        }

        lootConfig = new LootConfig();
        final var weapons_tier_0 = "weapons_tier_0";
        lootConfig.item_groups.put("weapons_tier_0", new LootConfig.ItemGroup(List.of(
                Weapons.acolyte_wand.id().toString(),
                Weapons.wooden_great_hammer.id().toString(),
                Weapons.stone_great_hammer.id().toString(),
                Weapons.stone_claymore.id().toString()),
                1
        ));

        final var weapons_one_handed_tier_1 = "weapons_one_handed_tier_1";
        lootConfig.item_groups.put(weapons_one_handed_tier_1, new LootConfig.ItemGroup(List.of(
                Weapons.holy_wand.id().toString(),
                Weapons.golden_mace.id().toString(),
                Weapons.iron_mace.id().toString()),
                1
        ).chance(0.3F));
        final var weapons_two_handed_tier_1 = "weapons_two_handed_tier_1";
        lootConfig.item_groups.put(weapons_two_handed_tier_1, new LootConfig.ItemGroup(List.of(
                Weapons.holy_staff.id().toString(),
                Weapons.iron_claymore.id().toString(),
                Weapons.iron_great_hammer.id().toString()),
                1
        ).chance(0.3F));

        final var weapons_one_handed_tier_2 = "weapons_one_handed_tier_2";
        lootConfig.item_groups.put(weapons_one_handed_tier_2, new LootConfig.ItemGroup(List.of(
                Weapons.netherite_holy_wand.id().toString(),
                Weapons.diamond_mace.id().toString()),
                1
        ).chance(0.3F));
        final var weapons_two_handed_tier_2 = "weapons_two_handed_tier_2";
        lootConfig.item_groups.put(weapons_two_handed_tier_2, new LootConfig.ItemGroup(List.of(
                Weapons.holy_staff.id().toString(),
                Weapons.diamond_claymore.id().toString(),
                Weapons.diamond_great_hammer.id().toString()),
                1
        ).chance(0.3F));

        final var armor_tier_1 = "armor_tier_1";
        lootConfig.item_groups.put(armor_tier_1, new LootConfig.ItemGroup(joinLists(
                Armors.paladinArmorSet_t1.idStrings(),
                Armors.priestArmorSet_t1.idStrings()),
                1
        ).chance(0.25F));
        final var armor_tier_2 = "armor_tier_2";
        lootConfig.item_groups.put(armor_tier_2, new LootConfig.ItemGroup(joinLists(
                Armors.paladinArmorSet_t2.idStrings(),
                Armors.priestArmorSet_t2.idStrings()),
                1
        ).chance(0.5F));


        List.of("minecraft:chests/spawn_bonus_chest",
                        "minecraft:chests/igloo_chest",
                        "minecraft:chests/ruined_portal",
                        "minecraft:chests/shipwreck_supply",
                        "minecraft:chests/jungle_temple")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_tier_0)));

        List.of("minecraft:chests/desert_pyramid",
                        "minecraft:chests/bastion_bridge",
                        "minecraft:chests/jungle_temple",
                        "minecraft:chests/pillager_outpost",
                        "minecraft:chests/underwater_ruin_small",
                        "minecraft:chests/stronghold_crossing")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_one_handed_tier_1)));

        List.of("minecraft:chests/nether_bridge")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_two_handed_tier_2)));

        List.of("minecraft:chests/shipwreck_supply",
                        "minecraft:chests/stronghold_corridor")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(armor_tier_1)));

        List.of("minecraft:chests/stronghold_library",
                        "minecraft:chests/underwater_ruin_big",
                        "minecraft:chests/bastion_other",
                        "minecraft:chests/woodland_mansion",
                        "minecraft:chests/simple_dungeon",
                        "minecraft:chests/underwater_ruin_big.json")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_one_handed_tier_1, armor_tier_1)));

        List.of("minecraft:chests/end_city_treasure",
                        "minecraft:chests/bastion_treasure",
                        "minecraft:chests/ancient_city",
                        "minecraft:chests/stronghold_library")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_two_handed_tier_2, armor_tier_2)));


        worldGenConfig = new WorldGenConfig();
        worldGenConfig.entries.addAll(List.of(
                new WorldGenConfig.Entry("minecraft:village/desert/houses", "paladins:village/desert/sanctuary", 4),
                new WorldGenConfig.Entry("minecraft:village/savanna/houses", "paladins:village/savanna/sanctuary", 3),
                new WorldGenConfig.Entry("minecraft:village/plains/houses", "paladins:village/plains/sanctuary", 4),
                new WorldGenConfig.Entry("minecraft:village/taiga/houses", "paladins:village/taiga/sanctuary", 4),
                new WorldGenConfig.Entry("minecraft:village/snowy/houses", "paladins:village/snowy/sanctuary", 4)
        ));
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
