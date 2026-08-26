package net.paladins.client.entity;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class BattleBannerEntityModel extends EntityModel<BannerEntityRenderer.State> {
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
		super(root, RenderTypes::entityCutout);
		this.battle_flag = root.getChild("battle_flag");
		this.flag_part = this.battle_flag.getChild("flag_part");
		this.flag_part_2 = this.flag_part.getChild("flag_part_2");
		this.flag_part_3 = this.flag_part_2.getChild("flag_part_3");
		this.flag_part_4 = this.flag_part_3.getChild("flag_part_4");
		this.placeAnimation = BattleBannerEntityAnimations.place.bake(root);
		this.idleAnimation = BattleBannerEntityAnimations.idle.bake(root);
	}

	private final KeyframeAnimation placeAnimation;
	private final KeyframeAnimation idleAnimation;
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition battle_flag = modelPartData.addOrReplaceChild("battle_flag", CubeListBuilder.create().texOffs(24, 38).addBox(-8.0F, -36.0F, -3.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(28, 0).addBox(-8.0F, -36.0F, -3.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(29, 23).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(45, 4).addBox(-1.0F, -38.0F, -1.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition flag_part = battle_flag.addOrReplaceChild("flag_part", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -34.0F, -2.0F));

		PartDefinition flag_part_2 = flag_part.addOrReplaceChild("flag_part_2", CubeListBuilder.create().texOffs(0, 8).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition flag_part_3 = flag_part_2.addOrReplaceChild("flag_part_3", CubeListBuilder.create().texOffs(0, 16).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition flag_part_4 = flag_part_3.addOrReplaceChild("flag_part_4", CubeListBuilder.create().texOffs(0, 24).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}

	// HAND-WRITTEN CODE

	public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(PaladinsMod.ID, "battle_banner"), "main");

	@Override
	public void setupAnim(BannerEntityRenderer.State state) {
		super.setupAnim(state);
		// Lifecycle-phase driven (cloud spawn_ticks/despawn_ticks = the clip's 43 ticks):
		// SPAWNING plays `place` forward, ACTIVE loops `idle` (seamless: `place` ends on
		// `idle`'s base pose), DESPAWNING plays `place` in reverse — its state clock counts
		// down to the phase end, so speed -1F samples the clip tail-to-head.
		this.placeAnimation.apply(state.spawnAnimationState,   state.ageInTicks,  1F);
		this.idleAnimation.apply(state.idleAnimationState,     state.ageInTicks,  1F);
		this.placeAnimation.apply(state.despawnAnimationState, state.ageInTicks, -1F);
	}
}
