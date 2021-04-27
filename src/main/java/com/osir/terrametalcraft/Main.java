package com.osir.terrametalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.osir.terrametalcraft.common.RegistryHandler;
import com.osir.terrametalcraft.common.item.ModItems;
import com.osir.terrametalcraft.common.recipe.RecipeHandler;
import com.osir.terrametalcraft.common.world.feature.ModFeatures;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class Main {
	public static final String MODID = "terrametalcraft";
	public static final String MODNAME = "Terra Metal Craft";
	public static final String VERSION = "1.0.0";

	private static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ItemGroup GROUP_ITEM = new ItemGroup(MODID + "_item") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.tmcCoin);
		}
	};

	public static final ItemGroup GROUP_EQUIPMENT = new ItemGroup(MODID + "_equipment") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.FURNACE);
		}
	};

	public Main() {
		RegistryHandler.setupItem();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModFeatures.FEATURES.register(bus);
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		RegistryHandler.registerTool();
		RegistryHandler.registerCapability();
		RecipeHandler.register();
		ModFeatures.init();
	}

	@SubscribeEvent
	public static void loadComplete(FMLLoadCompleteEvent event) {

	}
}