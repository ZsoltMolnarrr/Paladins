package net.paladins.client.armor;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.PaladinArmor;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PaladinArmorModel extends AnimatedGeoModel<PaladinArmor> {
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
