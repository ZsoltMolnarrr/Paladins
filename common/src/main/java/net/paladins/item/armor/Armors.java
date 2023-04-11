package net.paladins.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.paladins.PaladinsMod;
import net.paladins.item.Group;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Supplier;

public class Armors {
    private static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> { return Ingredient.ofItems(
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL);
    };

    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> paladinEntries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> priestEntries = new ArrayList<>();
    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }

    public static final Armor.Set paladinArmorSet_t1 =
            create(
                    new Armor.CustomMaterial(
                        "guard_armor",
                        10,
                        9,
                        PaladinArmor.equipSound,
                        WOOL_INGREDIENTS
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
                    new PaladinArmor(material, EquipmentSlot.HEAD, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.CHEST, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.LEGS, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.FEET, new Item.Settings().group(Group.PALADINS))
            ))
            .put(entries)
            .put(paladinEntries)
            .armorSet();

    public static final Armor.Set paladinArmorSet_t2 =
            create(
                    new Armor.CustomMaterial(
                            "paladin_armor",
                            10,
                            9,
                            PaladinArmor.equipSound,
                            WOOL_INGREDIENTS
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
                    new PaladinArmor(material, EquipmentSlot.HEAD, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.CHEST, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.LEGS, new Item.Settings().group(Group.PALADINS)),
                    new PaladinArmor(material, EquipmentSlot.FEET, new Item.Settings().group(Group.PALADINS))
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
                            PaladinArmor.equipSound,
                            WOOL_INGREDIENTS
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F)),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 0.5F))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PriestArmor(material, EquipmentSlot.HEAD, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.CHEST, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.LEGS, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.FEET, new Item.Settings().group(Group.PALADINS))
            ))
            .put(entries)
            .put(priestEntries)
            .armorSet();

    public static final Armor.Set priestArmorSet_t2 =
            create(
                    new Armor.CustomMaterial(
                            "prior_robe",
                            10,
                            9,
                            PaladinArmor.equipSound,
                            WOOL_INGREDIENTS
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1)),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(ItemConfig.SpellAttribute.bonuses(EnumSet.of(MagicSchool.HEALING), 1))
                    )
            )
            .bundle(material -> new Armor.Set(PaladinsMod.ID,
                    new PriestArmor(material, EquipmentSlot.HEAD, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.CHEST, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.LEGS, new Item.Settings().group(Group.PALADINS)),
                    new PriestArmor(material, EquipmentSlot.FEET, new Item.Settings().group(Group.PALADINS))
            ))
            .put(entries)
            .put(priestEntries)
            .armorSet();

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries);
    }
}

