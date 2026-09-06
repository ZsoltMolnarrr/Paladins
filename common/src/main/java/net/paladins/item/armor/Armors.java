package net.paladins.item.armor;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.Group;
import net.paladins.content.PaladinSounds;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(ArmorMaterial material, Identifier id, int durability,
                                      Armor.Set.ItemFactory factory, ArmorSetConfig defaults, int tier) {
        var entry = Armor.Entry.create(
                material,
                id,
                durability,
                factory,
                defaults,
                Equipment.LootProperties.of(tier)
        );
        entries.add(entry);
        return entry;
    }

    /// 1.20.1 has no armor-material registry and no `ArmorMaterial.Layer`: the material is a plain
    /// interface implementation and its `id` doubles as the texture (layer) id.
    public static ArmorMaterial material(
            String name, int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
            int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient) {

        return Armor.material(
                new Identifier(PaladinsMod.ID, name),
                Map.of(
                        ArmorItem.Type.HELMET, protectionHead,
                        ArmorItem.Type.CHESTPLATE, protectionChest,
                        ArmorItem.Type.LEGGINGS, protectionLegs,
                        ArmorItem.Type.BOOTS, protectionFeet),
                enchantability, equipSound, repairIngredient,
                0, 0
        );
    }

    
    private static final Identifier ATTACK_DAMAGE_ID = new Identifier("minecraft", "generic.attack_damage");
    private static final Identifier ARMOR_TOUGHNESS_ID = new Identifier("minecraft", "generic.armor_toughness");
    private static AttributeModifier damageMultiplier(float value) {
        return new AttributeModifier(
                ATTACK_DAMAGE_ID.toString(),
                value,
                EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    private static AttributeModifier toughnessBonus(float value) {
        return new AttributeModifier(
                ARMOR_TOUGHNESS_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADDITION);
    }

    public static ArmorMaterial paladin_armor = material(
            "paladin_armor",
            2, 6, 5, 2,
            9,
            PaladinSounds.paladin_armor_equip.entry(), () -> { return Ingredient.ofItems(Items.IRON_INGOT); });

    public static ArmorMaterial crusader_armor = material(
            "crusader_armor",
            3, 8, 6, 3,
            10,
            PaladinSounds.paladin_armor_equip.entry(), () -> { return Ingredient.ofItems(Items.GOLD_INGOT); });

    public static ArmorMaterial netherite_crusader_armor = material(
            "netherite_crusader_armor",
            3, 8, 6, 3,
            15,
            PaladinSounds.paladin_armor_equip.entry(), () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); });

    public static ArmorMaterial priest_robe = material(
            "priest_robe",
            1, 3, 2, 1,
            9,
            PaladinSounds.priest_robe_equip.entry(), () -> { return Ingredient.fromTag(ItemTags.WOOL); });

    public static ArmorMaterial prior_robe = material(
            "prior_robe",
            1, 3, 2, 1,
            10,
            PaladinSounds.priest_robe_equip.entry(), () -> { return Ingredient.ofItems(Items.GOLD_INGOT); });

    public static ArmorMaterial netherite_prior_robe = material(
            "netherite_prior_robe",
            1, 3, 2, 1,
            15,
            PaladinSounds.priest_robe_equip.entry(), () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); });

    private static final float paladin_t1_spell_power = 0.5F;
    private static final float paladin_t2_spell_power = 1F;
    private static final float paladin_t3_spell_power = 1F;
    private static final float paladin_t3_toughness = 1F;

    public static final Armor.Set paladinArmorSet_t1 = create(
            paladin_armor,
            new Identifier(PaladinsMod.ID, "paladin_armor"),
            15,
            PaladinArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(2)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t1_spell_power)),
                    new ArmorSetConfig.Piece(6)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t1_spell_power)),
                    new ArmorSetConfig.Piece(5)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t1_spell_power)),
                    new ArmorSetConfig.Piece(2)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t1_spell_power))
            ), 1)
            .translatedName("Paladin Helmet", "Paladin Chestplate", "Paladin Leggings", "Paladin Boots")
            .armorSet();

    public static final Armor.Set paladinArmorSet_t2 = create(
            crusader_armor,
            new Identifier(PaladinsMod.ID, "crusader_armor"),
            25,
            PaladinArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(3)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t2_spell_power)),
                    new ArmorSetConfig.Piece(8)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t2_spell_power)),
                    new ArmorSetConfig.Piece(6)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t2_spell_power)),
                    new ArmorSetConfig.Piece(3)
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t2_spell_power))
            ), 2)
            .translatedName("Crusader Helmet", "Crusader Chestplate", "Crusader Leggings", "Crusader Boots")
            .armorSet();

    public static final Armor.Set paladinArmorSet_t3 = create(
            netherite_crusader_armor,
            new Identifier(PaladinsMod.ID, "netherite_crusader_armor"),
            37,
            PaladinArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(3)
                            .add(toughnessBonus(paladin_t3_toughness))
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t3_spell_power)),
                    new ArmorSetConfig.Piece(8)
                            .add(toughnessBonus(paladin_t3_toughness))
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t3_spell_power)),
                    new ArmorSetConfig.Piece(6)
                            .add(toughnessBonus(paladin_t3_toughness))
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t3_spell_power)),
                    new ArmorSetConfig.Piece(3)
                            .add(toughnessBonus(paladin_t3_toughness))
                            .addAll(AttributeModifier.bonuses(List.of(SpellSchools.HEALING.id), paladin_t3_spell_power))

            ), 3)
            .translatedName("Netherite Crusader Helmet", "Netherite Crusader Chestplate", "Netherite Crusader Leggings", "Netherite Crusader Boots")
            .armorSet();

    private static final float priest_t1_spell_power = 0.2F;
    private static final float priest_t2_spell_power = 0.25F;
    private static final float priest_t2_haste = 0.03F;
    private static final float priest_t3_spell_power = 0.3F;
    private static final float priest_t3_haste = 0.04F;

    public static final Armor.Set priestArmorSet_t1 = create(
            priest_robe,
            new Identifier(PaladinsMod.ID, "priest_robe"),
            10,
            PriestArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(1)
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t1_spell_power)),
                    new ArmorSetConfig.Piece(3)
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t1_spell_power)),
                    new ArmorSetConfig.Piece(2)
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t1_spell_power)),
                    new ArmorSetConfig.Piece(1)
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t1_spell_power))
            ), 1)
            .translatedName("Priest Collar", "Priest Vestment", "Priest Trousers", "Priest Boots")
            .armorSet();

    public static final Armor.Set priestArmorSet_t2 = create(
            prior_robe,
            new Identifier(PaladinsMod.ID, "prior_robe"),
            20,
            PriestArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(1)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t2_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t2_haste)
                            )),
                    new ArmorSetConfig.Piece(3)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t2_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t2_haste)
                            )),
                    new ArmorSetConfig.Piece(2)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t2_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t2_haste)
                            )),
                    new ArmorSetConfig.Piece(1)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t2_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t2_haste)
                            ))
            ), 2)
            .translatedName("Prior Collar", "Prior Vestment", "Prior Trousers", "Prior Boots")
            .armorSet();

    public static final Armor.Set priestArmorSet_t3 = create(
            netherite_prior_robe,
            new Identifier(PaladinsMod.ID, "netherite_prior_robe"),
            30,
            PriestArmor::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(1)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t3_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t3_haste)
                            )),
                    new ArmorSetConfig.Piece(3)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t3_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t3_haste)
                            )),
                    new ArmorSetConfig.Piece(2)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t3_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t3_haste)
                            )),
                    new ArmorSetConfig.Piece(1)
                            .addAll(List.of(
                                    AttributeModifier.multiply(SpellSchools.HEALING.id, priest_t3_spell_power),
                                    AttributeModifier.multiply(SpellPowerMechanics.HASTE.id, priest_t3_haste)
                            ))
            ), 3)
            .translatedName("Netherite Prior Collar", "Netherite Prior Vestment", "Netherite Prior Trousers", "Netherite Prior Boots")
            .armorSet();

    public static void register(Map<String, ArmorSetConfig> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}

