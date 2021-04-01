package com.osir.terrametalcraft.common;

import com.github.zi_jing.cuckoolib.data.ModBlockTagsProvider;
import com.github.zi_jing.cuckoolib.data.ModItemTagsProvider;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.te.TEGrindstone;
import com.osir.terrametalcraft.common.te.TEStoneWorkTable;
import com.osir.terrametalcraft.data.ModRecipesProvider;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class RegisterEventHandler {
	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.REGISTERED_BLOCK.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
		TEStoneWorkTable.TYPE.setRegistryName(Main.MODID, "stone_work_table");
		TEGrindstone.TYPE.setRegistryName(Main.MODID, "grindstone");
		event.getRegistry().registerAll(TEStoneWorkTable.TYPE, TEGrindstone.TYPE);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		if (event.includeServer()) {
			DataGenerator gen = event.getGenerator();
			ExistingFileHelper helper = event.getExistingFileHelper();
			BlockTagsProvider providerBlock = new ModBlockTagsProvider(gen, Main.MODID, helper);
			gen.addProvider(providerBlock);
			gen.addProvider(new ModItemTagsProvider(gen, providerBlock, Main.MODID, helper));
			gen.addProvider(new ModRecipesProvider(gen));
		}
	}
}