package com.osir.terrametalcraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.client.model.entity.BulletModel;
import com.osir.terrametalcraft.common.entity.BulletEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
	private BulletModel model;

	public BulletRenderer(EntityRendererManager manager) {
		super(manager);
		this.model = new BulletModel();
	}

	@Override
	public ResourceLocation getTextureLocation(BulletEntity entity) {
		return new ResourceLocation(Main.MODID, "textures/block/stone_work_table_top.png");
	}

	@Override
	public void render(BulletEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		IVertexBuilder builder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot)));
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot)));
		this.model.renderToBuffer(matrixStack, builder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		matrixStack.popPose();
	}
}