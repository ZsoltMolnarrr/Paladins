package net.paladins.client.effect;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.render.OrbitingEffectRenderer;

import java.util.List;

public class DivineProtectionRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(PaladinsMod.ID, "effect/divine_protection");
    public static final Identifier modelId_overlay = new Identifier(PaladinsMod.ID, "effect/divine_protection_glow");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final RenderLayer GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.RADIATE, false);

    public DivineProtectionRenderer() {
        super(List.of(
                new Model(GLOWING_RENDER_LAYER, modelId_overlay),
                new Model(BASE_RENDER_LAYER, modelId_base)),
                1F,
                0.35F);
    }
}
