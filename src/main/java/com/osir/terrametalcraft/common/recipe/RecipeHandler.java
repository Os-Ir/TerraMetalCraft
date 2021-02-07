package com.osir.terrametalcraft.common.recipe;

import com.github.zi_jing.cuckoolib.material.MaterialUtil;
import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.recipe.IRecipeProperty;
import com.github.zi_jing.cuckoolib.recipe.NormalReicpeProperty;
import com.github.zi_jing.cuckoolib.recipe.RecipeMap;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeHandler {
	public static final RecipeMap MAP_CARVING = new RecipeMap("carving", 1, 1, 1, 1, 0, 0, 0, 0);

	public static final IRecipeProperty<Long> PROPERTY_CARVING = new NormalReicpeProperty<Long>(
			new ResourceLocation(Main.MODID, "carving"), 0l);

	public static void register() {
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedFlint))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.FLINT))
				.addPropertyValue(PROPERTY_CARVING, 1655735156992l).build(0);
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedFlint))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.FLINT))
				.addPropertyValue(PROPERTY_CARVING, 282453282304l).build(1);
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedStone))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.STONE))
				.addPropertyValue(PROPERTY_CARVING, 1655735156992l).build(2);
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedStone))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.STONE))
				.addPropertyValue(PROPERTY_CARVING, 282453282304l).build(3);
	}
}