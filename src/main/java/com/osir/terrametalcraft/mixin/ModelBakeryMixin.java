package com.osir.terrametalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.osir.terrametalcraft.Main;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
	private static final String PATH_PREFIX = "material_item.";

	@Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
	private void onGetModel(ResourceLocation location, CallbackInfoReturnable<IUnbakedModel> callback) {
		if (!location.getNamespace().equals(Main.MODID) || !location.getPath().startsWith(PATH_PREFIX)) {
			return;
		}
		String dat = location.getPath().substring(PATH_PREFIX.length());
		int p = dat.indexOf(".");
		String shape = dat.substring(0, p);
		IUnbakedModel model = ModelLoader.instance()
				.getModel(new ResourceLocation(Main.MODID, "item/material/" + shape));
		callback.setReturnValue(model);
		callback.cancel();
	}
}