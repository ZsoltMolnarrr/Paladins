package net.paladins.mixin.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.paladins.effect.DivineProtectionStatusEffect;
import net.paladins.effect.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityDivineProtection {

    // Mixin magic by TelepathicGrunt
    // When `argsOnly = true` is passed, ModifyVariable infers which argument we want to modify
    // based on the return value type
    @ModifyVariable(method = "modifyAppliedDamage", at = @At("HEAD"), argsOnly = true)
    private float modifyAppliedDamage_DivineProtection(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)
                || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || !entity.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
            return amount;
        }
        return amount * DivineProtectionStatusEffect.multiplier;
    }
}
