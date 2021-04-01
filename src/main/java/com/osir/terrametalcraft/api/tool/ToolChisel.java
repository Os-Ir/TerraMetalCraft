package com.osir.terrametalcraft.api.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.type.Material;
import com.github.zi_jing.cuckoolib.tool.ToolBase;
import com.osir.terrametalcraft.Main;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

public class ToolChisel extends ToolBase {
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
	public Map<Integer, Pair<String, Material>> getDefaultMaterial() {
		Map<Integer, Pair<String, Material>> map = new HashMap<Integer, Pair<String, Material>>();
		map.put(0, Pair.of("head", ModMaterials.IRON));
		map.put(1, Pair.of("handle", ModMaterials.WOOD));
		return map;
	}
}