package net.paladins.item;

import net.spell_engine.Platform;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.rpg_series.config.ShieldConfig;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Shield;
import net.spell_engine.rpg_series.item.Shields;

import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.spell_engine.rpg_series.config.AttributeModifier;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaladinShields {
    public static final ArrayList<Shield.Entry> entries = new ArrayList<>();

    private static Shield.Entry add(Shield.Entry entry) {
        entries.add(entry);
        return entry;
    }

    /// Standard tier attributes for kite shields.
    ///
    /// Deliberately *not* `Shields.standardAttributes(tier)`: SpellEngine still spells these attribute
    /// ids the 1.21.1 way (`minecraft:generic.armor_toughness`, `generic.max_health`), and 1.21.2
    /// dropped the `generic.` prefix — those ids no longer resolve (`ConfigUtil` logs
    /// "Failed to resolve EntityAttribute"). Values are identical to SpellEngine's.
    private static List<AttributeModifier> standardAttributes(Equipment.Tier tier) {
        return switch (tier) {
            case WOODEN, TIER_0, GOLDEN -> List.of();
            case TIER_1, TIER_2 -> List.of(toughness(1), health(2));
            case TIER_3 -> List.of(toughness(1), health(4));
            case TIER_4, TIER_5 -> List.of(toughness(1), health(6));
        };
    }

    private static AttributeModifier toughness(float value) {
        return new AttributeModifier(EntityAttributes.ARMOR_TOUGHNESS.getIdAsString(), value, Operation.ADD_VALUE);
    }

    private static AttributeModifier health(float value) {
        return new AttributeModifier(EntityAttributes.MAX_HEALTH.getIdAsString(), value, Operation.ADD_VALUE);
    }

    private static Shield.Entry create(String name, Equipment.Tier tier, @Nullable TagKey<Item> repairItems) {
        return Shields.create(PaladinsMod.ID, name, tier, repairItems,
                standardAttributes(tier), PaladinSounds.shield_equip.entry());
    }

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    // MARK: Shields

    public static Shield.Entry iron_kite_shield = add(create("iron_kite_shield", Equipment.Tier.TIER_1, ItemTags.IRON_TOOL_MATERIALS).translatedName("Iron Kite Shield"));
    public static Shield.Entry golden_kite_shield = add(create("golden_kite_shield", Equipment.Tier.GOLDEN, ItemTags.GOLD_TOOL_MATERIALS).translatedName("Golden Kite Shield"));
    public static Shield.Entry diamond_kite_shield = add(create("diamond_kite_shield", Equipment.Tier.TIER_2, ItemTags.DIAMOND_TOOL_MATERIALS).translatedName("Diamond Kite Shield"));
    public static Shield.Entry netherite_kite_shield = add(create("netherite_kite_shield", Equipment.Tier.TIER_3, ItemTags.NETHERITE_TOOL_MATERIALS).translatedName("Netherite Kite Shield"));

    public static void register(Map<String, ShieldConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            add(create("ruby_kite_shield", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_NETHER_RUBY));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            add(create("aeternium_kite_shield", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AETERNIUM));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            add(create("aether_kite_shield", Equipment.Tier.TIER_4, PaladinItemTags.REPAIRS_AMBROSIUM)
                    .loot(-1, "aether"));
        }
        Shield.register(configs, entries, Group.KEY);
    }
}
