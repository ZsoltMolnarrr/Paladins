package net.paladins.item.shield;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.RepairableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import net.spell_engine.rpg_series.item.Shield;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/// Shield items built purely from vanilla data components.
///
/// Since 1.21.2 every piece of shield behaviour is data driven, so no library and no custom
/// `Item` subclass is needed any more (this replaces the former `shield_api:CustomShieldItem`):
///
/// - **blocking** — `minecraft:blocks_attacks` (block delay, damage reduction cone, item damage per
///   blocked hit, the damage-type tag that bypasses the block, block/disable sounds)
/// - **off-hand slot + equip sound** — `minecraft:equippable` (unswappable, like the vanilla shield)
/// - **break sound** — `minecraft:break_sound`
/// - **durability** — `minecraft:max_damage` (from `Shield.Entry.durability()`)
/// - **repair** — `minecraft:repairable`
/// - **attributes** — `minecraft:attribute_modifiers` (from the shield config)
/// - **blocking model** — `assets/paladins/items/<shield>.json`, a `minecraft:condition` on
///   `minecraft:using_item` (the 1.21.4 replacement for the removed `blocking` model predicate)
public final class VanillaShields {

    private VanillaShields() { }

    /// Vanilla shield blocking: 0.25 s delay, 90 degree cone, full reduction,
    /// 3+ damage consumes durability, axes disable it, vanilla block/break sounds.
    public static final BlocksAttacksComponent VANILLA_SHIELD_BLOCKING = new BlocksAttacksComponent(
            0.25F,
            1.0F,
            List.of(new BlocksAttacksComponent.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
            new BlocksAttacksComponent.ItemDamage(3.0F, 1.0F, 1.0F),
            Optional.of(DamageTypeTags.BYPASSES_SHIELD),
            Optional.of(SoundEvents.ITEM_SHIELD_BLOCK),
            Optional.of(SoundEvents.ITEM_SHIELD_BREAK)
    );

    /// {@link Shield.ShieldFactory} implementation — pass as `VanillaShields::create`.
    public static Item create(
            @Nullable RegistryEntry<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributes,
            Item.Settings settings
    ) {
        var equippable = EquippableComponent.builder(EquipmentSlot.OFFHAND).swappable(false);
        if (equipSound != null) {
            equippable.equipSound(equipSound);
        }
        settings.component(DataComponentTypes.BLOCKS_ATTACKS, VANILLA_SHIELD_BLOCKING)
                .component(DataComponentTypes.EQUIPPABLE, equippable.build())
                .component(DataComponentTypes.BREAK_SOUND, SoundEvents.ITEM_SHIELD_BREAK)
                .attributeModifiers(attributesOf(attributes));

        // `Item.canRepair` is gone; repair is the REPAIRABLE component, resolved here (the same
        // point SpellEngine's `Weapon.CustomMaterial` resolves its own repair ingredient).
        var ingredient = repairIngredient.get();
        if (ingredient != null && !ingredient.isEmpty()) {
            settings.component(DataComponentTypes.REPAIRABLE,
                    new RepairableComponent(RegistryEntryList.of(ingredient.getMatchingItems().toList())));
        }
        return new Item(settings);
    }

    private static AttributeModifiersComponent attributesOf(
            List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributes) {
        var builder = AttributeModifiersComponent.builder();
        for (var pair : attributes) {
            builder.add(pair.getLeft(), pair.getRight(), AttributeModifierSlot.HAND);
        }
        return builder.build();
    }
}
