package com.osir.terrametalcraft.data;

import java.util.function.Consumer;

import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.recipe.IngredientIndex;
import com.github.zi_jing.cuckoolib.recipe.data.DataRecipeBuilder;
import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.material.MaterialUtil;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class ModRecipesProvider extends RecipeProvider {
	public ModRecipesProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> out) {
		DataRecipeBuilder.builder()
				.input(IngredientIndex.from(MaterialUtil.getMaterialTag(ModSolidShapes.DUST, ModMaterials.IRON), 2))
				.input(IngredientIndex.from(new ItemStack(Items.IRON_INGOT, 4)))
				.output(OutputItem.fromItemProvider(Items.GOLD_INGOT))
				.output(OutputItem.fromTag(MaterialUtil.getMaterialTag(ModSolidShapes.DUST, ModMaterials.GOLD), 16))
				.build(out, new ResourceLocation(Main.MODID, "test/ingot_test"), "test");
	}
}