package com.osir.terrametalcraft.common.recipe.builder;

import java.util.function.Consumer;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.google.gson.JsonObject;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.recipe.ModRecipeSerializers;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class StoneWorkBuilder extends RecipeBuilderBase<StoneWorkBuilder> {
	private Ingredient input;
	private OutputItem output;
	private long data;

	public StoneWorkBuilder(Ingredient input, OutputItem output, long data) {
		this.input = input;
		this.output = output;
		this.data = data;
	}

	public static StoneWorkBuilder builder(Ingredient input, OutputItem output, long data) {
		return new StoneWorkBuilder(input, output, data);
	}

	@Override
	public void build(Consumer<IFinishedRecipe> out, String id) {
		ResourceLocation advancementId = this.buildAdvancement(Main.MODID, id, "stone_work");
		out.accept(new FinishRecipe(new ResourceLocation(Main.MODID, "stone_work/" + id), advancementId));
	}

	private class FinishRecipe extends RecipeBuilderBase.FinishRecipeBase {
		private FinishRecipe(ResourceLocation id, ResourceLocation advancementId) {
			super(id, advancementId);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", StoneWorkBuilder.this.input.toJson());
			json.add("output", StoneWorkBuilder.this.output.serialize());
			json.addProperty("data", StoneWorkBuilder.this.data);
		}

		@Override
		public IRecipeSerializer<?> getType() {
			return ModRecipeSerializers.STONE_WORK_SERIALIZER;
		}
	}
}