package com.osir.terrametalcraft.common.recipe;

import com.osir.terrametalcraft.Main;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipeTypes {
	public static final IRecipeType<StoneWorkRecipe> STONE_WORK_TYPE = register("stone_work");

	private static <T extends IRecipe<?>> IRecipeType<T> register(String name) {
		return IRecipeType.register(Main.MODID + ":" + name);
	}
}