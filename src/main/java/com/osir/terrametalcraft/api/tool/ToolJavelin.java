package com.osir.terrametalcraft.api.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.github.zi_jing.cuckoolib.material.ModMaterials;
import com.github.zi_jing.cuckoolib.material.ModSolidShapes;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.tool.ToolBase;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.material.MaterialUtil;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ToolJavelin extends ToolBase implements IGrindstoneTool {
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "javelin");

	public static final ToolJavelin INSTANCE = new ToolJavelin();

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
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
		return ModItems.toolJavelin;
	}

	@Override
	public boolean matches(ItemStack[] parts) {
		Item item2 = parts[1].getItem();
		Item item3 = parts[2].getItem();
		return MaterialUtil.getMaterialTag(ModSolidShapes.JAVELIN_HEAD).contains(parts[0].getItem())
				&& (item2 == Items.STRING || item2 == ModItems.thinStrawRope) && (item3 == Items.STICK)
				&& parts[3].isEmpty() && parts[4].isEmpty();
	}

	@Override
	public Map<Integer, Pair<String, MaterialBase>> getMaterial(ItemStack[] parts) {
		Map<Integer, Pair<String, MaterialBase>> map = new HashMap<Integer, Pair<String, MaterialBase>>();
		Item item1 = parts[0].getItem();
		for (MaterialBase material : MaterialBase.REGISTRY.values()) {
			ITag<Item> tag = MaterialUtil.getMaterialTag(ModSolidShapes.JAVELIN_HEAD, material);
			if (tag != null && tag.contains(item1)) {
				map.put(0, Pair.of("head", material));
			}
		}
		map.put(1, Pair.of("handle", ModMaterials.WOOD));
		return map;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			float velocity = Math.min((72000 - timeLeft), 40) * 0.04f;
			System.out.println(velocity);
			if (!world.isClientSide && velocity >= 0.1f) {
				ArrowEntity arrow = new ArrowEntity(world, player);
				arrow.setBaseDamage(4);
				Vector3d vec = player.getLookAngle();
				arrow.shoot(vec.x, vec.y, vec.z, velocity, 3);
				world.addFreshEntity(arrow);
//				if (!player.abilities.isCreativeMode) {
//					stack.shrink(1);
//				}
			}
		}
	}
}