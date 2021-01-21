package com.osir.terrametalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.osir.terrametalcraft.Main;

import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.ResourceLocation;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void onLoadModel(ResourceLocation location, CallbackInfoReturnable<BlockModel> callback) {
		if (!location.getNamespace().equals(Main.MODID) || !location.getPath().startsWith("item/material_item")) {
			return;
		}
		String dat = location.getPath().substring(19);
		int p = dat.indexOf(".");
		String shape = dat.substring(0, p);
		String jsonText = "{\n	\"parent\": \"item/generated\",\n	\"textures\": {\n		\"layer0\": \"" + Main.MODID
				+ ":item/material/" + shape + "\"\n	}\n}";
		BlockModel model = BlockModel.deserialize(jsonText);
		model.name = location.toString();
		callback.setReturnValue(model);
		callback.cancel();
	}
}