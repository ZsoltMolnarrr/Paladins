package net.paladins.client.armor;

import mod.azure.azurelibarmor.model.GeoModel;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.armor.PriestArmor;

public class PriestArmorModel extends GeoModel<PriestArmor> {
    @Override
    public Identifier getModelResource(PriestArmor object) {
        return new Identifier(PaladinsMod.ID, "geo/priest_robes.geo.json");
    }

    @Override
    public Identifier getTextureResource(PriestArmor armor) {
        var texture = armor.customMaterial.name();
        return new Identifier(PaladinsMod.ID, "textures/armor/" + texture + ".png");
    }

    @Override
    public Identifier getAnimationResource(PriestArmor animatable) {
        return null; // new Identifier(PaladinsMod.ID, "animations/armor_idle.json");
    }
}
