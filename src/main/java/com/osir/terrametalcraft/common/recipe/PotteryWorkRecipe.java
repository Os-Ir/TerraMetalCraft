package com.osir.terrametalcraft.common.recipe;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.osir.terrametalcraft.common.recipe.serializer.ModRecipeSerializers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class PotteryWorkRecipe extends RecipeBase {
	public Ingredient input;
	public OutputItem output;
	public int[] data;

	public PotteryWorkRecipe(ResourceLocation id, Ingredient input, OutputItem output, int[] data) {
		super(id);
		this.input = input;
		this.output = output;
		this.data = data;
	}

	public boolean matches(ItemStack input) {
		return this.input.test(input);
	}

	@Override
	public ItemStack getResultItem() {
		return this.output.get();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.POTTERY_WORK_SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipeTypes.POTTERY_WORK_TYPE;
	}
}