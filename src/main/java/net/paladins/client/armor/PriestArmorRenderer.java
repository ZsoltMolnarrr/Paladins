package net.paladins.client.armor;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PriestArmorRenderer extends AzArmorRenderer {
    public static PriestArmorRenderer priest() {
        return new PriestArmorRenderer("priest_robes", "priest_robe");
    }
    public static PriestArmorRenderer prior() {
        return new PriestArmorRenderer("priest_robes", "prior_robe");
    }
    public static PriestArmorRenderer netheritePrior() {
        return new PriestArmorRenderer("priest_robes", "netherite_prior_robe");
    }

    public PriestArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(PaladinsMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(PaladinsMod.ID, "textures/armor/" + textureName + ".png")
        ).build());
    }
}
