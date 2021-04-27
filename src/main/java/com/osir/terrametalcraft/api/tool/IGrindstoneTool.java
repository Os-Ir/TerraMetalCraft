package com.osir.terrametalcraft.api.tool;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.tool.IToolInfo;

import net.minecraft.item.ItemStack;

public interface IGrindstoneTool extends IToolInfo {
	int getToolComplexity();

	MaterialToolItem getToolItem(ItemStack[] parts);

	boolean matches(ItemStack[] parts);

	Map<Integer, Pair<String, MaterialBase>> getMaterial(ItemStack[] parts);
}