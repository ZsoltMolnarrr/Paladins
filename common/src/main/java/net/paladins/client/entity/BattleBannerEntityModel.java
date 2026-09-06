package net.paladins.client.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.entity.BannerEntity;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class BattleBannerEntityModel extends SinglePartEntityModel<BannerEntity> {
	private final ModelPart root;
	private final ModelPart battle_flag;
	private final ModelPart flag_part;
	private final ModelPart flag_part_2;
	private final ModelPart flag_part_3;
	private final ModelPart flag_part_4;

	public BattleBannerEntityModel(ModelPart root) {
		// Backface-culled layer, overriding the default `getEntityCutoutNoCull`. The flag panels are
		// zero-thickness cuboids, so each one's north and south faces are coplanar; drawn without culling
		// they z-fight against each other. Culling keeps only the camera-facing face of each pair, so the
		// flag still shows from both sides but the two coplanar quads never fight.
		super(RenderLayer::getEntityCutout);
		this.root = root;
		this.battle_flag = root.getChild("battle_flag");
		this.flag_part = this.battle_flag.getChild("flag_part");
		this.flag_part_2 = this.flag_part.getChild("flag_part_2");
		this.flag_part_3 = this.flag_part_2.getChild("flag_part_3");
		this.flag_part_4 = this.flag_part_3.getChild("flag_part_4");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData battle_flag = modelPartData.addChild("battle_flag", ModelPartBuilder.create().uv(24, 38).cuboid(-8.0F, -36.0F, -3.0F, 16.0F, 2.0F, 2.0F, new Dilation(0.2F))
		.uv(28, 0).cuboid(-8.0F, -36.0F, -3.0F, 16.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(29, 23).cuboid(-1.5F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(45, 4).cuboid(-1.0F, -38.0F, -1.0F, 2.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData flag_part = battle_flag.addChild("flag_part", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -34.0F, -2.0F));

		ModelPartData flag_part_2 = flag_part.addChild("flag_part_2", ModelPartBuilder.create().uv(0, 8).cuboid(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

		ModelPartData flag_part_3 = flag_part_2.addChild("flag_part_3", ModelPartBuilder.create().uv(0, 16).cuboid(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

		ModelPartData flag_part_4 = flag_part_3.addChild("flag_part_4", ModelPartBuilder.create().uv(0, 24).cuboid(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	// HAND-WRITTEN CODE

	public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier(PaladinsMod.ID, "battle_banner"), "main");

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(BannerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		// Lifecycle-phase driven (cloud spawn_ticks/despawn_ticks = the clip's 43 ticks):
		// SPAWNING plays `place` forward, ACTIVE loops `idle` (seamless: `place` ends on
		// `idle`'s base pose), DESPAWNING plays `place` in reverse — its state clock counts
		// down to the phase end, so speed -1F samples the clip tail-to-head.
		this.updateAnimation(entity.spawnAnimationState,   BattleBannerEntityAnimations.place, ageInTicks, 1F);
		this.updateAnimation(entity.idleAnimationState,    BattleBannerEntityAnimations.idle,  ageInTicks, 1F);
		this.updateAnimation(entity.despawnAnimationState, BattleBannerEntityAnimations.place, ageInTicks, -1F);
	}
}
