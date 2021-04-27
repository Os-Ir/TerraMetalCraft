package com.osir.terrametalcraft.api.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.github.zi_jing.cuckoolib.material.MaterialUtil;
import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.tool.ToolBase;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class ToolChisel extends ToolBase implements IGrindstoneTool {
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "chisel");

	public static final ToolChisel INSTANCE = new ToolChisel();

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(BlockState state) {
		return false;
	}

	@Override
	public Map<Integer, Pair<String, MaterialBase>> getDefaultMaterial() {
		Map<Integer, Pair<String, MaterialBase>> map = new HashMap<Integer, Pair<String, MaterialBase>>();
		map.put(0, Pair.of("head", ModMaterials.IRON));
		map.put(1, Pair.of("handle", ModMaterials.WOOD));
		return map;
	}

	@Override
	public int getToolComplexity() {
		return 3;
	}

	@Override
	public MaterialToolItem getToolItem(ItemStack[] parts) {
		return ModItems.toolChisel;
	}

	@Override
	public boolean matches(ItemStack[] parts) {
		Item item2 = parts[1].getItem();
		Item item3 = parts[2].getItem();
		return MaterialUtil.getMaterialTag(ModSolidShapes.CHISEL_HEAD).contains(parts[0].getItem())
				&& (item2 == Items.STRING || item2 == ModItems.thinStrawRope) && (item3 == Items.STICK)
				&& parts[3].isEmpty() && parts[4].isEmpty();
	}

	@Override
	public Map<Integer, Pair<String, MaterialBase>> getMaterial(ItemStack[] parts) {
		Map<Integer, Pair<String, MaterialBase>> map = new HashMap<Integer, Pair<String, MaterialBase>>();
		Item item1 = parts[0].getItem();
		for (MaterialBase material : MaterialBase.REGISTRY.values()) {
			ITag<Item> tag = MaterialUtil.getMaterialTag(ModSolidShapes.CHISEL_HEAD, material);
			if (tag != null && tag.contains(item1)) {
				map.put(0, Pair.of("head", material));
			}
		}
		map.put(1, Pair.of("handle", ModMaterials.WOOD));
		return map;
	}
}