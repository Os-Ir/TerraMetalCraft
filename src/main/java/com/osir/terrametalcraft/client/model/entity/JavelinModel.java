package com.osir.terrametalcraft.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.osir.terrametalcraft.common.entity.JavelinEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class JavelinModel extends EntityModel<JavelinEntity> {
	private ModelRenderer head, handle;

	public JavelinModel() {
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-1.5f, -1.5f, -1.5f, 3, 3, 3);
		this.head.setPos(0, 0, 0);
		this.handle = new ModelRenderer(this, 0, 0);
		this.handle.addBox(-0.5f, -0.5f, -11.5f, 1, 1, 10);
		this.handle.setPos(0, 0, 0);
	}

	@Override
	public void setupAnim(JavelinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.handle.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}