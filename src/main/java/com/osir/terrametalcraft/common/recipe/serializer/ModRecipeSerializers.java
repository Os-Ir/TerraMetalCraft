package com.osir.terrametalcraft.common.recipe.serializer;

import com.osir.terrametalcraft.Main;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MODID);

	public static final StoneWorkSerializer STONE_WORK_SERIALIZER = new StoneWorkSerializer();
	public static final PotteryWorkSerializer POTTERY_WORK_SERIALIZER = new PotteryWorkSerializer();

	public static final RegistryObject<StoneWorkSerializer> _STONE_WORK_SERIALIZER = RECIPE_SERIALIZERS.register("stone_work", () -> STONE_WORK_SERIALIZER);
	public static final RegistryObject<PotteryWorkSerializer> _POTTERY_WORK_SERIALIZER = RECIPE_SERIALIZERS.register("pottery_work", () -> POTTERY_WORK_SERIALIZER);
}