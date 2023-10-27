package net.paladins.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.DamageTypeTags;
import net.paladins.effect.DivineProtectionStatusEffect;
import net.paladins.effect.Effects;
import net.paladins.util.SoundHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityDivineProtection {

    // Mixin magic by TelepathicGrunt
    // When `argsOnly = true` is passed, ModifyVariable infers which argument we want to modify
    // based on the return value type

//    @ModifyVariable(method = "modifyAppliedDamage", at = @At("HEAD"), argsOnly = true)
//    private float modifyAppliedDamage_DivineProtection(float amount, DamageSource source) {
//        LivingEntity entity = (LivingEntity) (Object) this;
//        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)
//                || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
//                || !entity.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
//            return amount;
//        }
//        return amount * DivineProtectionStatusEffect.multiplier;
//    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage_HEAD_DivineProtection(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();
        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)
                || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || attacker == null
                || amount <= 0
                || entity.getWorld().isClient()) {
            return;
        }
        if (entity.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
            cir.cancel();
            var instance = entity.getStatusEffect(Effects.DIVINE_PROTECTION);
            if (instance != null) {
                // Remove current instance
                entity.removeStatusEffect(Effects.DIVINE_PROTECTION);
                if (instance.getAmplifier() > 0) {
                    // Add a new instance with a lower amplifier
                    entity.addStatusEffect(
                            new StatusEffectInstance(Effects.DIVINE_PROTECTION,
                                    instance.getDuration(),
                                    instance.getAmplifier() - 1,
                                    instance.isAmbient(),
                                    instance.shouldShowParticles(),
                                    instance.shouldShowIcon())
                    );
                }
            }
            DivineProtectionStatusEffect.pop(entity);
            SoundHelper.playSoundEvent(entity.getWorld(), entity, SoundHelper.divineProtectionImpact);

            // Copied from LivingEntity.java (`damage` method), where it is called `takeKnockback`
//            double d = attacker.getX() - entity.getX();
//            double e;
//            for(e = attacker.getZ() - entity.getZ(); d * d + e * e < 1.0E-4; e = (Math.random() - Math.random()) * 0.01) {
//                d = (Math.random() - Math.random()) * 0.01;
//            }
//            System.out.println("ASD Knockback: " + d + ", " + e);
//            entity.takeKnockback(0.4, d, e);
        }
    }

//    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
//    private float damage_DivineProtection(float amount, DamageSource source) {
//        LivingEntity entity = (LivingEntity) (Object) this;
//        Entity attacker = source.getAttacker();
//        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)
//                || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
//                || attacker == null
//                || entity.getWorld().isClient()) {
//            return amount;
//        }
//
//        if (entity.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
//            var instance = entity.getStatusEffect(Effects.DIVINE_PROTECTION);
//            if (instance != null) {
//                // Remove current instance
//                entity.removeStatusEffect(Effects.DIVINE_PROTECTION);
//                if (instance.getAmplifier() > 0) {
//                    // Add a new instance with a lower amplifier
//                    entity.addStatusEffect(
//                            new StatusEffectInstance(Effects.DIVINE_PROTECTION,
//                                    instance.getDuration(),
//                                    instance.getAmplifier() - 1,
//                                    instance.isAmbient(),
//                                    instance.shouldShowParticles(),
//                                    instance.shouldShowIcon())
//                    );
//                }
//            }
//            SoundHelper.playSoundEvent(entity.getWorld(), entity, SoundHelper.divineProtectionImpact);
//            return 0;
//        }
//
//        return amount;
//    }
}
