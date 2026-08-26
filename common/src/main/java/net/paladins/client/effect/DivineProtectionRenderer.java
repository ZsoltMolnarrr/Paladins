package net.paladins.client.effect;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.render.OrbitingEffectRenderer;

import java.util.List;

public class DivineProtectionRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = Identifier.fromNamespaceAndPath(PaladinsMod.ID, "spell_effect/divine_protection");
    public static final Identifier modelId_overlay = Identifier.fromNamespaceAndPath(PaladinsMod.ID, "spell_effect/divine_protection_glow");

    // `RenderLayer.getEntityTranslucent` is gone; SpellEngine's own block-atlas spell-object layer
    // is the 1.21.11 equivalent (translucent, backface-culled, not part of the entity outline).
    private static final RenderType BASE_RENDER_LAYER =
            CustomLayers.spellObject(LightEmission.NONE);
    private static final RenderType GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.RADIATE, false);

    public DivineProtectionRenderer() {
        super(List.of(
                new Model(GLOWING_RENDER_LAYER, modelId_overlay),
                new Model(BASE_RENDER_LAYER, modelId_base)),
                1F,
                0.35F);
    }
}
