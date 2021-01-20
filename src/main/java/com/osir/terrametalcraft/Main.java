package com.osir.terrametalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.zi_jing.cuckoolib.gui.CapabilityListener;
import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.common.EventHandler;
import com.osir.terrametalcraft.common.RegistryHandler;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
		RegistryHandler.setupItem();
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public void setup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		RegistryHandler.registerCapability();
		CapabilityListener.register(CapabilityCarving.KEY, ModCapabilities.CARVING);
	}

	public void loadComplete(FMLLoadCompleteEvent event) {

	}

	public void serverStarting(FMLServerStartingEvent event) {

	}
}