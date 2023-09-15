package net.paladins.client.armor;

import mod.azure.azurelibarmor.model.GeoModel;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.armor.PaladinArmor;

public class PaladinArmorModel extends GeoModel<PaladinArmor> {
    @Override
    public Identifier getModelResource(PaladinArmor object) {
        return new Identifier(PaladinsMod.ID, "geo/paladin_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(PaladinArmor armor) {
        var texture = armor.customMaterial.name();
        return new Identifier(PaladinsMod.ID, "textures/armor/" + texture + ".png");
    }

    @Override
    public Identifier getAnimationResource(PaladinArmor animatable) {
        return null; // new Identifier(PaladinsMod.ID, "animations/armor_idle.json");
    }
}
