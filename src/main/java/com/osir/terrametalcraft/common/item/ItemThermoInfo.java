package com.osir.terrametalcraft.common.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.zi_jing.cuckoolib.item.IItemTipInfo;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;

public class ItemThermoInfo implements IItemTipInfo {
	@Override
	public String getTitle(ItemStack stack) {
		return I18n.get("cuckoolib.tipinfo.thermo.title");
	}

	@Override
	public List<String> getInfo(ItemStack stack) {
		LazyOptional<IHeatable> optional = stack.getCapability(ModCapabilities.HEATABLE);
		if (!optional.isPresent()) {
			return new ArrayList<String>();
		}
		return Arrays.asList(
				TextFormatting.YELLOW + I18n.get("cuckoolib.tipinfo.thermo.temperature") + TextFormatting.GOLD
						+ String.format("%.1f", optional.orElse(null).getTemperature()) + TextFormatting.RED + "K");
	}

	@Override
	public TextFormatting getIndexColor(ItemStack stack) {
		return TextFormatting.RED;
	}
}