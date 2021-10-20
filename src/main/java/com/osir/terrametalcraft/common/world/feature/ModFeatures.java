package com.osir.terrametalcraft.common.world.feature;

import com.osir.terrametalcraft.Main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
			Main.MODID);

	public static final RegistryObject<StoneFeature> STONE_FEATURE = FEATURES.register("stone",
			() -> new StoneFeature(NoFeatureConfig.CODEC));

	public static ConfiguredFeature<NoFeatureConfig, ?> stone;

	public static void init() {
		stone = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Main.MODID, "stone"),
				STONE_FEATURE.get().configured(NoFeatureConfig.NONE));
	}
}