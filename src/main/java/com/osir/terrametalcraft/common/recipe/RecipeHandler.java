package com.osir.terrametalcraft.common.recipe;

import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.recipe.IRecipeProperty;
import com.github.zi_jing.cuckoolib.recipe.NormalReicpeProperty;
import com.github.zi_jing.cuckoolib.recipe.RecipeMap;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.material.MaterialUtil;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeHandler {
	public static final RecipeMap MAP_CARVING = new RecipeMap("carving", 1, 1, 1, 1, 0, 0, 0, 0);
	public static final RecipeMap MAP_GRINDSTONE = new RecipeMap("grindstone", 1, 5, 1, 1, 0, 0, 0, 0);

	public static final IRecipeProperty<Long> PROPERTY_CARVING = new NormalReicpeProperty<Long>(
			new ResourceLocation(Main.MODID, "carving"), 0l);

	public static void register() {
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedFlint))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.FLINT))
				.addPropertyValue(PROPERTY_CARVING, 1655735156992l).build(Main.MODID, "flint_knife_head");
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedStone))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.KNIFE_HEAD, ModMaterials.STONE))
				.addPropertyValue(PROPERTY_CARVING, 1655735156992l).build(Main.MODID, "stone_knife_head");
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedFlint))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.FLINT))
				.addPropertyValue(PROPERTY_CARVING, 282453282304l).build(Main.MODID, "flint_chisel_head");
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.grindedStone))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.CHISEL_HEAD, ModMaterials.STONE))
				.addPropertyValue(PROPERTY_CARVING, 282453282304l).build(Main.MODID, "stone_chisel_head");
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.chippedFlint))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.HAMMER_HEAD, ModMaterials.FLINT))
				.addPropertyValue(PROPERTY_CARVING, 1023744l).build(Main.MODID, "flint_hammer_head");
		MAP_CARVING.builder().inputItem(new ItemStack(ModItems.chippedStone))
				.outputItem(MaterialUtil.getMaterialItemStack(ModSolidShapes.HAMMER_HEAD, ModMaterials.STONE))
				.addPropertyValue(PROPERTY_CARVING, 1023744l).build(Main.MODID, "stone_hammer_head");

//		Material.REGISTRY.values().forEach((material) -> {
//			if (material.hasFlag(Material.GENERATE_TOOL)) {
//				MAP_GRINDSTONE.builder().input(IngredientIndex.from(ModSolidShapes.HAMMER_HEAD, material))
//						.inputItem(new ItemStack(ModItems.thinStrawRope)).inputItem(new ItemStack(Items.STICK))
//						.outputItem(MaterialToolItem.setToolMaterial(ModItems.toolHammer.createItemStack(), 0, "head",
//								material))
//						.build(Main.MODID, material.getName() + "_hammer");
//				MAP_GRINDSTONE.builder().input(IngredientIndex.from(ModSolidShapes.CHISEL_HEAD, material))
//						.inputItem(new ItemStack(ModItems.thinStrawRope)).inputItem(new ItemStack(Items.STICK))
//						.outputItem(MaterialToolItem.setToolMaterial(ModItems.toolChisel.createItemStack(), 0, "head",
//								material))
//						.build(Main.MODID, material.getName() + "_chisel");
//				MAP_GRINDSTONE.builder().input(IngredientIndex.from(ModSolidShapes.KNIFE_HEAD, material))
//						.inputItem(new ItemStack(ModItems.thinStrawRope)).inputItem(new ItemStack(Items.STICK))
//						.outputItem(MaterialToolItem.setToolMaterial(ModItems.toolKnife.createItemStack(), 0, "head",
//								material))
//						.build(Main.MODID, material.getName() + "_knife");
//			}
//		});
	}
}