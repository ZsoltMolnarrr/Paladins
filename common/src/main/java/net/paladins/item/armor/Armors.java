package net.paladins.item.armor;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
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

    /// Since 1.21.2 `ArmorMaterial` is a plain record (no registry) whose `durability` is the
    /// per-slot multiplier, repair is a `TagKey<Item>` (the `minecraft:repairable` component) and
    /// rendering is keyed by an `EquipmentAsset` id instead of a texture-layer list. We deliberately
    /// point every set at a `paladins:` asset id that no `assets/paladins/equipment/*.json` defines:
    /// the vanilla humanoid armor layers then resolve to `EquipmentModel.EMPTY` (silently — see
    /// `EquipmentModelLoader`), leaving the geo models from Armor Model API as the only armor drawn.
    public static ArmorMaterial material(
            String name, int durability,
            int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
            int enchantability, RegistryEntry<SoundEvent> equipSound, TagKey<Item> repairIngredient) {

        return new ArmorMaterial(
                durability,
                Map.of(
                        EquipmentType.HELMET, protectionHead,
                        EquipmentType.CHESTPLATE, protectionChest,
                        EquipmentType.LEGGINGS, protectionLegs,
                        EquipmentType.BOOTS, protectionFeet),
                enchantability,
                equipSound,
                0F,
                0F,
                repairIngredient,
                assetKey(name)
        );
    }

    private static RegistryKey<EquipmentAsset> assetKey(String name) {
        return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(PaladinsMod.ID, name));
    }

    
    private static final Identifier ATTACK_DAMAGE_ID = Identifier.ofVanilla("generic.attack_damage");
    private static final Identifier ARMOR_TOUGHNESS_ID = Identifier.ofVanilla("generic.armor_toughness");
    private static AttributeModifier damageMultiplier(float value) {
        return new AttributeModifier(
                ATTACK_DAMAGE_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    private static AttributeModifier toughnessBonus(float value) {
        return new AttributeModifier(
                ARMOR_TOUGHNESS_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static ArmorMaterial paladin_armor = material(
            "paladin_armor",
            15,
            2, 6, 5, 2,
            9,
            PaladinSounds.paladin_armor_equip.entry(), ItemTags.REPAIRS_IRON_ARMOR);

    public static ArmorMaterial crusader_armor = material(
            "crusader_armor",
            25,
            3, 8, 6, 3,
            10,
            PaladinSounds.paladin_armor_equip.entry(), ItemTags.REPAIRS_GOLD_ARMOR);

    public static ArmorMaterial netherite_crusader_armor = material(
            "netherite_crusader_armor",
            37,
            3, 8, 6, 3,
            15,
            PaladinSounds.paladin_armor_equip.entry(), ItemTags.REPAIRS_NETHERITE_ARMOR);

    public static ArmorMaterial priest_robe = material(
            "priest_robe",
            10,
            1, 3, 2, 1,
            9,
            PaladinSounds.priest_robe_equip.entry(), ItemTags.WOOL);

    public static ArmorMaterial prior_robe = material(
            "prior_robe",
            20,
            1, 3, 2, 1,
            10,
            PaladinSounds.priest_robe_equip.entry(), ItemTags.REPAIRS_GOLD_ARMOR);

    public static ArmorMaterial netherite_prior_robe = material(
            "netherite_prior_robe",
            30,
            1, 3, 2, 1,
            15,
            PaladinSounds.priest_robe_equip.entry(), ItemTags.REPAIRS_NETHERITE_ARMOR);

    private static final float paladin_t1_spell_power = 0.5F;
    private static final float paladin_t2_spell_power = 1F;
    private static final float paladin_t3_spell_power = 1F;
    private static final float paladin_t3_toughness = 1F;

    public static final Armor.Set paladinArmorSet_t1 = create(
            paladin_armor,
            Identifier.of(PaladinsMod.ID, "paladin_armor"),
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
            Identifier.of(PaladinsMod.ID, "crusader_armor"),
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
            Identifier.of(PaladinsMod.ID, "netherite_crusader_armor"),
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
            Identifier.of(PaladinsMod.ID, "priest_robe"),
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
            Identifier.of(PaladinsMod.ID, "prior_robe"),
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
            Identifier.of(PaladinsMod.ID, "netherite_prior_robe"),
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

