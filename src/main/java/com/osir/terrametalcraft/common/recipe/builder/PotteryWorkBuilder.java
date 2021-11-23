package com.osir.terrametalcraft.common.recipe.builder;

import java.util.function.Consumer;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.recipe.serializer.ModRecipeSerializers;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class PotteryWorkBuilder extends RecipeBuilderBase<PotteryWorkBuilder> {
	private Ingredient input;
	private OutputItem output;
	private int[] data;

	public PotteryWorkBuilder(Ingredient input, OutputItem output, int[] data) {
		this.input = input;
		this.output = output;
		this.data = data;
	}

	public static PotteryWorkBuilder builder(Ingredient input, OutputItem output, int[] data) {
		return new PotteryWorkBuilder(input, output, data);
	}

	@Override
	public void build(Consumer<IFinishedRecipe> out, String id) {
		ResourceLocation advancementId = this.buildAdvancement(Main.MODID, id, "pottery_work");
		out.accept(new FinishRecipe(new ResourceLocation(Main.MODID, "pottery_work/" + id), advancementId));
	}

	private class FinishRecipe extends RecipeBuilderBase.FinishRecipeBase {
		private FinishRecipe(ResourceLocation id, ResourceLocation advancementId) {
			super(id, advancementId);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", PotteryWorkBuilder.this.input.toJson());
			json.add("output", PotteryWorkBuilder.this.output.serialize());
			JsonArray array = new JsonArray();
			for (int d : PotteryWorkBuilder.this.data) {
				array.add(d);
			}
			json.add("data", array);
		}

		@Override
		public IRecipeSerializer<?> getType() {
			return ModRecipeSerializers.POTTERY_WORK_SERIALIZER;
		}
	}
}