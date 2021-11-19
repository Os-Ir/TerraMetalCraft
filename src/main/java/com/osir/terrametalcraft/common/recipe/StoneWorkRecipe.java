package com.osir.terrametalcraft.common.recipe;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class StoneWorkRecipe extends RecipeBase {
	public Ingredient input;
	public OutputItem output;
	public long data;

	public StoneWorkRecipe(ResourceLocation id, Ingredient input, OutputItem output, long data) {
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
		return ModRecipeSerializers.STONE_WORK_SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipeTypes.STONE_WORK_TYPE;
	}
}