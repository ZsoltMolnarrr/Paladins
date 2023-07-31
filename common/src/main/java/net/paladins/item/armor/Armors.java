package net.paladins.item.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.paladins.PaladinsMod;
import net.paladins.item.Group;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> paladinEntries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> priestEntries = new ArrayList<>();
    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }

    public static final Armor.Set paladinArmorSet_t1 =
            create(
                    new Armor.CustomMaterial(
                        "paladin_armor",
                        15,
                        9,
                        PaladinArmor.equipSound,
                        () -> { return Ingredient.ofItems(Items.IRON_INGOT); }
                    ),
                    ItemConfig.ArmorSet.with(
                        new ItemConfig.ArmorSet.Piece(2)
                                .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                        new ItemConfig.ArmorSet.Piece(6)
                                .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                        new ItemConfig.ArmorSet.Piece(5)
                                .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                        new ItemConfig.ArmorSet.Piece(2)
                                .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PaladinArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .put(paladinEntries)
            .armorSet();

    public static final Armor.Set paladinArmorSet_t2 =
            create(
                    new Armor.CustomMaterial(
                            "crusader_armor",
                            25,
                            10,
                            PaladinArmor.equipSound,
                            () -> { return Ingredient.ofItems(Items.GOLD_INGOT); }
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(5)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PaladinArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new PaladinArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .put(paladinEntries)
            .armorSet();

    public static final Armor.Set priestArmorSet_t1 =
            create(
                    new Armor.CustomMaterial(
                            "priest_robe",
                            10,
                            9,
                            PriestArmor.equipSound,
                            () -> { return Ingredient.fromTag(ItemTags.WOOL); }
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1F)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1F)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1F)),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1F))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PriestArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .put(priestEntries)
            .armorSet();

    public static float priestRobeHaste = 0.05F;
    private static final float specializedRobeSpellPower = 0.25F;
    public static final Armor.Set priestArmorSet_t2 =
            create(
                    new Armor.CustomMaterial(
                            "prior_robe",
                            20,
                            10,
                            PriestArmor.equipSound,
                            () -> { return Ingredient.ofItems(Items.GOLD_INGOT); }
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.HEALING), specializedRobeSpellPower),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, priestRobeHaste)
                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.HEALING), specializedRobeSpellPower),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, priestRobeHaste)
                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.HEALING), specializedRobeSpellPower),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, priestRobeHaste)
                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.HEALING), specializedRobeSpellPower),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, priestRobeHaste)
                                    ))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PriestArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new PriestArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .put(priestEntries)
            .armorSet();

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}

