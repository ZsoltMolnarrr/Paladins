package net.paladins.client.armor;

import mod.azure.azurelibarmor.renderer.GeoArmorRenderer;
import net.paladins.item.armor.PriestArmor;

public class PriestArmorRenderer extends GeoArmorRenderer<PriestArmor> {
    public PriestArmorRenderer() {
        super(new PriestArmorModel());

        //These values are what each bone name is in blockbench. So if your head bone is named "bone545",
        // make sure to do this.headBone = "bone545";

        // The default values are the ones that come with the default armor template in the geckolib blockbench plugin.
//        this.headBone = "armorHead";
//        this.bodyBone = "armorBody";
//        this.rightArmBone = "armorRightArm";
//        this.leftArmBone = "armorLeftArm";
//        this.rightLegBone = "armorRightLeg";
//        this.leftLegBone = "armorLeftLeg";
//        this.rightBootBone = "armorRightBoot";
//        this.leftBootBone = "armorLeftBoot";
    }

//    @Override
//    public RenderLayer getRenderType(PriestArmor animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
//        return RenderLayer.getEntityTranslucent(texture);
//    }
}
