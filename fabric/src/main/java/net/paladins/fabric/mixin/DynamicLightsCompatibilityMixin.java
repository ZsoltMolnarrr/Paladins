package net.paladins.fabric.mixin;

import net.paladins.entity.LightwellEntity;
import net.spell_engine.fabric.client.compat.DynamicLightsCompatibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/// Registers the Paladins {@link LightwellEntity} as a LambDynLights light source by appending to Spell
/// Engine's {@link DynamicLightsCompatibility#registrations()} list, so the well brightens the area it
/// heals in. Piggy-backing on Spell Engine's compat class (rather than shipping our own
/// {@code DynamicLightsInitializer}) means this only ever runs when LambDynLights is installed: that class
/// is loaded solely by LambDynLights' entrypoint, so absent LambDynLights the target never loads and this
/// mixin never applies. The appended entry carries only a {@code ToIntFunction} — no LambDynLights type —
/// so nothing here forces LambDynLights onto the classpath.
/// {@code remap = false}: the target is a Spell Engine method, not a Minecraft one, so the mixin AP must
/// not try to look it up in the obfuscation mappings.
@Mixin(value = DynamicLightsCompatibility.class, remap = false)
public class DynamicLightsCompatibilityMixin {
    /// Full holy radiance. The well is a bright, stationary fixture; a max light level reads as it
    /// casting light over its surroundings for the duration it stands.
    private static final int LIGHTWELL_LUMINANCE = 15;

    @Inject(method = "registrations", at = @At("RETURN"), remap = false)
    private static void paladins$addLightwell(CallbackInfoReturnable<List<DynamicLightsCompatibility.Registration<?>>> cir) {
        cir.getReturnValue().add(new DynamicLightsCompatibility.Registration<>(
                LightwellEntity.TYPE,
                entity -> LIGHTWELL_LUMINANCE));
    }
}
