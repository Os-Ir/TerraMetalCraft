package com.osir.terrametalcraft.common;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.te.TEStoneWorkTable;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class RegisterEventHandler {
	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.REGISTERED_BLOCK.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
		TEStoneWorkTable.TYPE.setRegistryName(Main.MODID, "stone_work_table");
		event.getRegistry().registerAll(TEStoneWorkTable.TYPE);
	}
}