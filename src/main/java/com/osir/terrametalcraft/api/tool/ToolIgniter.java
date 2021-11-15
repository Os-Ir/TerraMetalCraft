package com.osir.terrametalcraft.api.tool;

import java.util.List;

import com.github.zi_jing.cuckoolib.item.ToolItem;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.tool.IToolUsable;
import com.github.zi_jing.cuckoolib.tool.ToolHarvestDisable;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ToolIgniter extends ToolHarvestDisable implements IToolUsable {
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Main.MODID, "igniter");

	public static final ToolIgniter INSTANCE = new ToolIgniter();

	@Override
	public ResourceLocation getRegistryName() {
		return REGISTRY_NAME;
	}

	@Override
	public boolean validateMaterial(MaterialBase material) {
		return false;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 25600;
	}

	@Override
	public int getInteractionDamage() {
		return 0;
	}

	@Override
	public int getContainerCraftDamage() {
		return 0;
	}

	@Override
	public int getEntityHitDamage() {
		return 0;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClientSide) {
			return ActionResult.success(player.getItemInHand(hand));
		}
		ItemStack stack = player.getItemInHand(hand);
		IHeatable cap = stack.getCapability(ModCapabilities.HEATABLE).orElse(null);
		if (player.isCrouching() && cap.getTemperature() <= 550) {
			cap.increaseEnergy(20000);
			this.getToolItem(stack).damageItem(stack, 100);
		}
		return ActionResult.success(stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		ToolItem item = this.getToolItem(stack);
		int damage = item.getToolDamage(stack);
		int maxDamage = item.getToolMaxDamage(stack);
		float durability = ((float) damage) / maxDamage;
		TextFormatting color = TextFormatting.GREEN;
		if (durability >= 0.7 && durability < 0.9) {
			color = TextFormatting.YELLOW;
		} else if (durability >= 0.9 && durability < 0.97) {
			color = TextFormatting.RED;
		} else if (durability >= 0.97) {
			color = System.currentTimeMillis() % 500 < 250 ? TextFormatting.RED : TextFormatting.WHITE;
		}
		StringBuilder builder = new StringBuilder();
		int num = (int) ((1 - durability) * 20);
		builder.append(color);
		for (int i = 0; i < num; i++) {
			builder.append("=");
		}
		builder.append(">");
		builder.append(TextFormatting.WHITE);
		for (int i = 0; i < 20 - num - 1; i++) {
			builder.append("-");
		}
		tooltip.add(new StringTextComponent(color + "" + (maxDamage - damage) + TextFormatting.WHITE + " / " + maxDamage + " " + builder));
	}
}