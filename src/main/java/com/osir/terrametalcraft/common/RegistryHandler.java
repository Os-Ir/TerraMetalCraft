package com.osir.terrametalcraft.common;

import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.ICarving;
import com.osir.terrametalcraft.api.capability.StorageCarving;
import com.osir.terrametalcraft.common.block.BlockStoneWorkTable;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.item.ModItems;
import com.osir.terrametalcraft.common.tool.ToolHammer;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class RegistryHandler {
	public static void setupItem() {
		ModItems.tmcCoin = new ItemBase(Main.MODID, "tmc_coin", Main.GROUP_ITEM);
		ModItems.stone = new ItemBase(Main.MODID, "stone", Main.GROUP_ITEM);
		ModItems.chippedStone = new ItemBase(Main.MODID, "chipped_stone", Main.GROUP_ITEM);
		ModItems.grindedStone = new ItemBase(Main.MODID, "grinded_stone", Main.GROUP_ITEM);
		ModItems.chippedFlint = new ItemBase(Main.MODID, "chipped_flint", Main.GROUP_ITEM);
		ModItems.grindedFlint = new ItemBase(Main.MODID, "grinded_flint", Main.GROUP_ITEM);
		ModItems.grass = new ItemBase(Main.MODID, "grass", Main.GROUP_ITEM);
		ModItems.hay = new ItemBase(Main.MODID, "hay", Main.GROUP_ITEM);
		ModItems.toolHammer = new MaterialToolItem(Main.MODID, "hammer", Main.GROUP_ITEM, ToolHammer.INSTANCE);

		ModBlocks.stoneWorkTable = new BlockStoneWorkTable();
	}

	public static void registerCapability() {
		CapabilityManager.INSTANCE.register(ICarving.class, new StorageCarving(), CapabilityCarving::new);
	}
}