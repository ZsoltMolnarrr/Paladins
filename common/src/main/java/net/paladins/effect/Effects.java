package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.Synchronized;

public class Effects {
    public static StatusEffect DIVINE_PROTECTION = new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff);

//    public static StatusEffect frozen = new FrozenStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff)
//            .setVulnerability(MagicSchool.FROST, new SpellPower.Vulnerability(0, 1F, 0F))
//            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
//                    "052f3166-8ae7-11ed-a1eb-0242ac120002",
//                    -1F,
//                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
//
//    public static StatusEffect frostShield = new FrostShieldStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff)
//            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
//                    "0563d59a-8c60-11ed-a1eb-0242ac120002",
//                    -0.5F,
//                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public static void register() {
        ((Synchronized) DIVINE_PROTECTION).setSynchronized(true);
//
        int rawId = 710;
        Registry.register(Registry.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "divine_protection").toString(), DIVINE_PROTECTION);
//        Registry.register(Registry.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "frost_shield").toString(), frostShield);
    }
}
