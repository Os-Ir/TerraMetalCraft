package com.osir.terrametalcraft.common;

import com.github.zi_jing.cuckoolib.LibRegistryHandler;
import com.github.zi_jing.cuckoolib.gui.CapabilityListener;
import com.osir.terrametalcraft.api.ModRegistries;
import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.CapabilityHeatable;
import com.osir.terrametalcraft.api.capability.ICarving;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.capability.StorageCarving;
import com.osir.terrametalcraft.api.capability.StorageHeatable;
import com.osir.terrametalcraft.api.tool.ToolChisel;
import com.osir.terrametalcraft.api.tool.ToolHammer;
import com.osir.terrametalcraft.api.tool.ToolKnife;
import com.osir.terrametalcraft.common.item.ItemThermoInfo;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class RegistryHandler {
//	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER = DeferredRegister
//			.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Main.MODID);

//	public static final RegistryObject<GrassDropSerializer> GRASS_DROP = LOOT_MODIFIER.register("grass_drop",
//			GrassDropSerializer::new);

	public static void register() {
		LibRegistryHandler.ITEM_TIP_INFO.register((stack) -> stack.getCapability(ModCapabilities.HEATABLE).isPresent(), new ItemThermoInfo());
	}

	public static void registerTool() {
		ModRegistries.REGISTRY_TOOL.register(ToolHammer.REGISTRY_NAME, ToolHammer.INSTANCE);
		ModRegistries.REGISTRY_TOOL.register(ToolChisel.REGISTRY_NAME, ToolChisel.INSTANCE);
		ModRegistries.REGISTRY_TOOL.register(ToolKnife.REGISTRY_NAME, ToolKnife.INSTANCE);
	}

	public static void registerCapability() {
		CapabilityManager.INSTANCE.register(ICarving.class, new StorageCarving(), CapabilityCarving::new);
		CapabilityManager.INSTANCE.register(IHeatable.class, new StorageHeatable(), CapabilityHeatable::new);
		CapabilityListener.register(CapabilityCarving.KEY, ModCapabilities.CARVING);
		CapabilityListener.register(CapabilityHeatable.KEY, ModCapabilities.HEATABLE);
	}
}