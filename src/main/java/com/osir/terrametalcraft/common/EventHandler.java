package com.osir.terrametalcraft.common;

import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.ICarving;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		Item item = stack.getItem();
		if (!stack.getCapability(ModCapabilities.CARVING).isPresent() && (item == ModItems.chippedFlint
				|| item == ModItems.chippedStone || item == ModItems.grindedFlint || item == ModItems.grindedStone)) {
			event.addCapability(CapabilityCarving.KEY, new CapabilityCarving());
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void addItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		LazyOptional<ICarving> optional = stack.getCapability(ModCapabilities.CARVING);
		if (optional.isPresent() && !optional.orElse(null).isEmpty()) {
			e.getToolTip()
					.add(new StringTextComponent(TextFormatting.AQUA + I18n.format("cuckoolib.item.carving.carved")));
		}
	}
}