package com.osir.terrametalcraft.data;

import java.util.function.Consumer;

import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.osir.terrametalcraft.api.material.MaterialUtil;
import com.osir.terrametalcraft.common.item.ModItems;
import com.osir.terrametalcraft.common.recipe.builder.StoneWorkBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ModRecipesProvider extends RecipeProvider {
	public ModRecipesProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> out) {
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.GRINDED_FLINT)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.FLINT)), 1655735156992l).build(out, "flint_knife_head");
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.GRINDED_STONE)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.STONE)), 1655735156992l).build(out, "stone_knife_head");
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.GRINDED_FLINT)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.FLINT)), 282453282304l).build(out, "flint_chisel_head");
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.GRINDED_STONE)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.STONE)), 282453282304l).build(out, "stone_chisel_head");
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.CHIPPED_FLINT)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.HAMMER_HEAD, ModMaterials.FLINT)), 1023744l).build(out, "flint_hammer_head");
		StoneWorkBuilder.builder(Ingredient.of(new ItemStack(ModItems.CHIPPED_STONE)), OutputItem.fromItemStack(MaterialUtil.getMaterialItemStack(ModSolidShapes.HAMMER_HEAD, ModMaterials.STONE)), 1023744l).build(out, "stone_hammer_head");
	}
}