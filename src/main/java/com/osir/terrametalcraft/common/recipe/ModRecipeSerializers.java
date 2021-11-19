package com.osir.terrametalcraft.common.recipe;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.recipe.serializer.StoneWorkSerializer;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MODID);

	public static final StoneWorkSerializer STONE_WORK_SERIALIZER = new StoneWorkSerializer();

	public static final RegistryObject<StoneWorkSerializer> _STONE_WORK_SERIALIZER = RECIPE_SERIALIZERS.register("stone_work", () -> STONE_WORK_SERIALIZER);
}