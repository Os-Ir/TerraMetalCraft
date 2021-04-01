package com.osir.terrametalcraft.api.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.type.Material;
import com.github.zi_jing.cuckoolib.tool.ToolBase;
import com.github.zi_jing.cuckoolib.tool.ToolUtil;
import com.osir.terrametalcraft.Main;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

public class ToolKnife extends ToolBase {
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "knife");

	public static final ToolKnife INSTANCE = new ToolKnife();

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(BlockState state) {
		ToolType tool = state.getHarvestTool();
		if (tool != null && (tool.getName().equals("sword") || tool.getName().equals("knife"))) {
			return true;
		}
		return ToolUtil.canSwordHarvest(state.getMaterial());
	}

	@Override
	public Map<Integer, Pair<String, Material>> getDefaultMaterial() {
		Map<Integer, Pair<String, Material>> map = new HashMap<Integer, Pair<String, Material>>();
		map.put(0, Pair.of("head", ModMaterials.IRON));
		map.put(1, Pair.of("handle", ModMaterials.WOOD));
		return map;
	}
}