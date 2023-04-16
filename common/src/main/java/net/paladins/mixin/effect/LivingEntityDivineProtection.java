package net.paladins.mixin.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.paladins.effect.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityDivineProtection {
    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void modifyAppliedDamage_DivineProtection(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (source.isUnblockable() || !entity.hasStatusEffect(Effects.DIVINE_PROTECTION)) {
            return;
        }

        float returnValue = cir.getReturnValue();
        cir.setReturnValue(returnValue * 0.5F);
    }
}
