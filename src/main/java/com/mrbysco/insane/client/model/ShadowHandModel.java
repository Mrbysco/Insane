package com.mrbysco.insane.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ShadowHandModel extends EntityModel<Entity> {
	private final ModelRenderer forearm;
	private final ModelRenderer wrist;
	private final ModelRenderer fingers;
	private final ModelRenderer ellbow;
	private final ModelRenderer arm;

	public ShadowHandModel() {
		textureWidth = 64;
		textureHeight = 32;

		forearm = new ModelRenderer(this);
		forearm.setRotationPoint(-0.5F, 22.5F, 0.0F);
		forearm.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -8.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

		wrist = new ModelRenderer(this);
		wrist.setRotationPoint(0.0F, 0.0F, -8.0F);
		forearm.addChild(wrist);
		wrist.setTextureOffset(0, 20).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);

		fingers = new ModelRenderer(this);
		fingers.setRotationPoint(-1.5F, 1.25F, 0.5F);
		wrist.addChild(fingers);
		fingers.setTextureOffset(0, 27).addBox(2.25F, -0.25F, -6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		fingers.setTextureOffset(0, 27).addBox(2.5F, -4.25F, -6.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		fingers.setTextureOffset(0, 27).addBox(0.5F, -4.75F, -6.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		fingers.setTextureOffset(0, 27).addBox(-0.75F, -2.75F, -6.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		ellbow = new ModelRenderer(this);
		ellbow.setRotationPoint(-0.5F, 22.5F, 0.0F);
		ellbow.setTextureOffset(20, 0).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, 0.1F, false);

		arm = new ModelRenderer(this);
		arm.setRotationPoint(0.5F, 22.5F, 0.0F);
		arm.setTextureOffset(0, 10).addBox(-2.5F, -1.5F, 1.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		forearm.render(matrixStack, buffer, packedLight, packedOverlay);
		ellbow.render(matrixStack, buffer, packedLight, packedOverlay);
		arm.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}