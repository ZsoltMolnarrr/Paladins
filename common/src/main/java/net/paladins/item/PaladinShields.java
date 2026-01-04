package net.paladins.item;

import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ShieldConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.Tiers;
import net.spell_engine.api.item.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PaladinShields {
    public static class Holder { public Holder() {}; public Holder(Item item) { this.item = item; }; public Item item; }
    public record Entry(Identifier id, Supplier<Ingredient> repair, List<AttributeModifier> attributes, int durability, Equipment.LootProperties lootProperties, Holder holder) {  }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    public static Entry shield(String name, Supplier<Ingredient> repair, List<AttributeModifier> attributes, int durability, Equipment.LootProperties lootProperties) {
        var entry = new Entry(Identifier.of(PaladinsMod.ID, name), repair, attributes, durability, lootProperties, new Holder());
        ENTRIES.add(entry);
        return entry;
    }

    private static final String GENERIC_ARMOR_TOUGHNESS = "minecraft:generic.armor_toughness";
    private static final String GENERIC_MAX_HEALTH = "generic.max_health";

    private static final int durability_t0 = 168;
    private static final int durability_t1 = 336; // Matches vanilla shield
    private static final int durability_t2 = 672;
    private static final int durability_t3 = 1344;
    private static final int durability_t4 = 2688;

    public static Entry iron_kite_shield = shield("iron_kite_shield",
            () -> Ingredient.ofItems(Items.IRON_INGOT),
            List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  2.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ),
            durability_t1, Equipment.LootProperties.of(1));
    public static Entry golden_kite_shield = shield("golden_kite_shield",
            () -> Ingredient.ofItems(Items.GOLD_INGOT), List.of(
            ),
            durability_t0, Equipment.LootProperties.of("golden_weapon"));
    public static Entry diamond_kite_shield = shield("diamond_kite_shield",
            () -> Ingredient.ofItems(Items.DIAMOND), List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  2.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ),
            durability_t2, Equipment.LootProperties.of(2));
    public static Entry netherite_kite_shield = shield("netherite_kite_shield",
            () -> Ingredient.ofItems(Items.NETHERITE_INGOT), List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  4.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ),
            durability_t3, Equipment.LootProperties.of(3));

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    public static void register(Map<String, ShieldConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            shield("ruby_kite_shield", repair, List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  6.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ), durability_t4, Equipment.LootProperties.of(4));
        }

        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            shield("aeternium_kite_shield", repair, List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  6.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ), durability_t4, Equipment.LootProperties.of(4));
        }

        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(AETHER)) {
            var repair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            shield("aether_kite_shield", repair, List.of(
                    new AttributeModifier(GENERIC_ARMOR_TOUGHNESS,  1,  EntityAttributeModifier.Operation.ADD_VALUE),
                    new AttributeModifier(GENERIC_MAX_HEALTH,  6.0f,  EntityAttributeModifier.Operation.ADD_VALUE)
            ), durability_t4, Equipment.LootProperties.of("aether"));
        }

        var netheriteTier = Tiers.unsafe("netherite");
        ArrayList<Item> shields = new ArrayList<>();
        for (var entry: ENTRIES) {
            var config = configs.get(entry.id.toString());
            if (config == null) {
                config = new ShieldConfig();
                config.durability = entry.durability;
                config.attributes = entry.attributes;
                configs.put(entry.id.toString(), config);
            }
            ArrayList<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> shieldAttributes = new ArrayList<>();
            for (var modifier: Weapon.attributesFrom(config.attributes).modifiers()) {
                shieldAttributes.add(new Pair<>(modifier.attribute(), modifier.modifier()));
            }
            var settings = new Item.Settings().maxDamage(config.durability);
            var tier = entry.lootProperties().tier();
            if (tier >= netheriteTier) {
                settings.fireproof();
            }
            var shield = new CustomShieldItem(PaladinSounds.shield_equip.entry(), entry.repair, shieldAttributes, settings);
            entry.holder.item = shield;
            Registry.register(Registries.ITEM, entry.id, shield);
            shields.add(shield);
        }

        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var shield: shields) {
                content.add(shield);
            }
        });
    }
}
