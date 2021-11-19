package com.osir.terrametalcraft.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class RecipeBase implements IRecipe<IInventory> {
	protected ResourceLocation id;

	public RecipeBase(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public boolean matches(IInventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack assemble(IInventory inventory) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
}