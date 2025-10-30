package net.paladins.client.armor;

import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererConfig;
import mod.azure.azurelibarmor.common.render.layer.AzArmorTrimLayer;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PaladinArmorRenderer extends AzArmorRenderer {
    public static PaladinArmorRenderer paladin() {
        return new PaladinArmorRenderer("paladin_armor", "paladin_armor");
    }
    public static PaladinArmorRenderer crusader() {
        return new PaladinArmorRenderer("paladin_armor", "crusader_armor");
    }
    public static PaladinArmorRenderer netheriteCrusader() {
        return new PaladinArmorRenderer("paladin_armor", "netherite_crusader_armor");
    }

    public PaladinArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(PaladinsMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(PaladinsMod.ID, "textures/armor/" + textureName + ".png"))
                .addRenderLayer(new AzArmorTrimLayer(Identifier.of(PaladinsMod.ID, "armor/trim/" + textureName + "_generic"), false))
                .build());
    }
}
