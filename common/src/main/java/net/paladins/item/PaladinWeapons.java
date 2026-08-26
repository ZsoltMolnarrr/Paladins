package net.paladins.item;

import net.spell_engine.Platform;
import net.minecraft.tags.ItemTags;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSpells;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.rpg_series.item.Weapons;

import java.util.ArrayList;
import java.util.Map;

public class PaladinWeapons {
    private static final String NAMESPACE = PaladinsMod.ID;
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry add(Weapon.Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    // MARK: Claymores

    public static final Weapon.Entry stone_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "stone_claymore", Equipment.Tier.TIER_0, null)
            .translatedName("Stone Claymore"));
    public static final Weapon.Entry iron_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "iron_claymore", Equipment.Tier.TIER_1, null)
            .translatedName("Iron Claymore"));
    public static final Weapon.Entry golden_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "golden_claymore", Equipment.Tier.GOLDEN, null)
            .translatedName("Golden Claymore"));
    public static final Weapon.Entry diamond_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "diamond_claymore", Equipment.Tier.TIER_2, null)
            .translatedName("Diamond Claymore"));
    public static final Weapon.Entry netherite_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "netherite_claymore", Equipment.Tier.TIER_3, null)
            .translatedName("Netherite Claymore"));

    // MARK: Hammers

    public static final Weapon.Entry wooden_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "wooden_great_hammer", Equipment.Tier.WOODEN, null)
            .translatedName("Wooden Great Hammer"));
    public static final Weapon.Entry stone_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "stone_great_hammer", Equipment.Tier.TIER_0, null)
            .translatedName("Stone Great Hammer"));
    public static final Weapon.Entry iron_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "iron_great_hammer", Equipment.Tier.TIER_1, null)
            .translatedName("Iron Great Hammer"));
    public static final Weapon.Entry golden_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "golden_great_hammer", Equipment.Tier.GOLDEN, null)
            .translatedName("Golden Great Hammer"));
    public static final Weapon.Entry diamond_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "diamond_great_hammer", Equipment.Tier.TIER_2, null)
            .translatedName("Diamond Great Hammer"));
    public static final Weapon.Entry netherite_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "netherite_great_hammer", Equipment.Tier.TIER_3, null)
            .translatedName("Netherite Great Hammer"));

    // MARK: Maces

    public static final Weapon.Entry iron_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "iron_mace", Equipment.Tier.TIER_1, null)
            .translatedName("Iron Mace"));
    public static final Weapon.Entry golden_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "golden_mace", Equipment.Tier.GOLDEN, null)
            .translatedName("Golden Mace"));
    public static final Weapon.Entry diamond_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "diamond_mace", Equipment.Tier.TIER_2, null)
            .translatedName("Diamond Mace"));
    public static final Weapon.Entry netherite_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "netherite_mace", Equipment.Tier.TIER_3, null)
            .translatedName("Netherite Mace"));

    // MARK: Wands

    public static final Weapon.Entry acolyte_wand = add(Weapons.healingWand(
            NAMESPACE, "acolyte_wand", Equipment.Tier.TIER_0, PaladinItemTags.REPAIRS_STICK)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Acolyte Wand"));
    public static final Weapon.Entry holy_wand = add(Weapons.healingWand(
            NAMESPACE, "holy_wand", Equipment.Tier.TIER_1, ItemTags.GOLD_TOOL_MATERIALS)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Holy Wand"));
    public static final Weapon.Entry diamond_holy_wand = add(Weapons.healingWand(
            NAMESPACE, "diamond_holy_wand", Equipment.Tier.TIER_2, null)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Diamond Holy Wand"));
    public static final Weapon.Entry netherite_holy_wand = add(Weapons.healingWand(
            NAMESPACE, "netherite_holy_wand", Equipment.Tier.TIER_3, null)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Netherite Holy Wand"));

    // MARK: Staves

    public static final Weapon.Entry holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "holy_staff", Equipment.Tier.TIER_1, ItemTags.GOLD_TOOL_MATERIALS)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Holy Staff"));
    public static final Weapon.Entry diamond_holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "diamond_holy_staff", Equipment.Tier.TIER_2, null)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Diamond Holy Staff"));
    public static final Weapon.Entry netherite_holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "netherite_holy_staff", Equipment.Tier.TIER_3, null)
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Netherite Holy Staff"));

    // MARK: Register

    public static void register(Map<String, WeaponConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            add(Weapons.healingStaff(NAMESPACE, "ruby_holy_staff", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_NETHER_RUBY)
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id())));
            add(Weapons.claymoreWithSkill(NAMESPACE, "ruby_claymore", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_NETHER_RUBY));
            add(Weapons.hammerWithSkill(NAMESPACE, "ruby_great_hammer", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_NETHER_RUBY));
            add(Weapons.maceWithSkill(NAMESPACE, "ruby_mace", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_NETHER_RUBY));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            add(Weapons.claymoreWithSkill(NAMESPACE, "aeternium_claymore", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AETERNIUM));
            add(Weapons.hammerWithSkill(NAMESPACE, "aeternium_great_hammer", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AETERNIUM));
            add(Weapons.maceWithSkill(NAMESPACE, "aeternium_mace", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AETERNIUM));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            add(Weapons.healingStaff(NAMESPACE, "aether_holy_staff", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AMBROSIUM)
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.claymoreWithSkill(NAMESPACE, "aether_claymore", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AMBROSIUM)
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.hammerWithSkill(NAMESPACE, "aether_great_hammer", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AMBROSIUM)
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.maceWithSkill(NAMESPACE, "aether_mace", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AMBROSIUM)
                    .loot(Equipment.LootProperties.of("aether")));
        }

        Weapon.register(configs, entries, Group.KEY);
    }
}
