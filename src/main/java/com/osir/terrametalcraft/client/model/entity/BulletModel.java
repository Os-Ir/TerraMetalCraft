package com.osir.terrametalcraft.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.osir.terrametalcraft.common.entity.BulletEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BulletModel extends EntityModel<BulletEntity> {
	private ModelRenderer model;

	public BulletModel() {
		this.model = new ModelRenderer(this, 0, 0);
		this.model.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1);
		this.model.setPos(0, 0, 0);
	}

	@Override
	public void setupAnim(BulletEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.model.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}