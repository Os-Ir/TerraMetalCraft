package com.osir.terrametalcraft.common.util.loot;

import java.util.List;

import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

public class GrassDropModifier extends LootModifier {
	public GrassDropModifier(ILootCondition[] condition) {
		super(condition);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(ModItems.grass));
		return generatedLoot;
	}
}