package com.osir.terrametalcraft.common;

import com.osir.terrametalcraft.common.item.ItemBase;
import com.osir.terrametalcraft.common.item.ModItems;

public class RegistryHandler {
	public static void setupItem() {
		ModItems.tmcCoin = new ItemBase("tmc_coin");
		ModItems.stone = new ItemBase("stone");
	}
}