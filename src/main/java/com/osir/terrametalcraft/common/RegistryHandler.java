package com.osir.terrametalcraft.common;

import com.github.zi_jing.cuckoolib.LibRegistryHandler;
import com.github.zi_jing.cuckoolib.gui.CapabilityListener;
import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.github.zi_jing.cuckoolib.material.SolidShape;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.Main;
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
import com.osir.terrametalcraft.api.tool.ToolJavelin;
import com.osir.terrametalcraft.api.tool.ToolKnife;
import com.osir.terrametalcraft.common.block.BlockCampfire;
import com.osir.terrametalcraft.common.block.BlockCoverGrass;
import com.osir.terrametalcraft.common.block.BlockCoverItem;
import com.osir.terrametalcraft.common.block.BlockGrindstone;
import com.osir.terrametalcraft.common.block.BlockStoneWorkTable;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.item.ItemGun;
import com.osir.terrametalcraft.common.item.ItemThermoInfo;
import com.osir.terrametalcraft.common.item.MaterialItem;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class RegistryHandler {
//	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER = DeferredRegister
//			.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Main.MODID);

//	public static final RegistryObject<GrassDropSerializer> GRASS_DROP = LOOT_MODIFIER.register("grass_drop",
//			GrassDropSerializer::new);

	public static void register() {
		LibRegistryHandler.ITEM_TIP_INFO.register((stack) -> stack.getCapability(ModCapabilities.HEATABLE).isPresent(),
				new ItemThermoInfo());
	}

	public static void setupItem() {
		ModItems.tmcCoin = new ItemBase(Main.MODID, "tmc_coin", Main.GROUP_ITEM);
		ModItems.stone = new ItemBase(Main.MODID, "stone", Main.GROUP_ITEM);
		ModItems.chippedStone = new ItemBase(Main.MODID, "chipped_stone", Main.GROUP_ITEM);
		ModItems.grindedStone = new ItemBase(Main.MODID, "grinded_stone", Main.GROUP_ITEM);
		ModItems.chippedFlint = new ItemBase(Main.MODID, "chipped_flint", Main.GROUP_ITEM);
		ModItems.grindedFlint = new ItemBase(Main.MODID, "grinded_flint", Main.GROUP_ITEM);
		ModItems.grass = new ItemBase(Main.MODID, "grass", Main.GROUP_ITEM);
		ModItems.hay = new ItemBase(Main.MODID, "hay", Main.GROUP_ITEM);
		ModItems.leaves = new ItemBase(Main.MODID, "leaves", Main.GROUP_ITEM);
		ModItems.thinStrawRope = new ItemBase(Main.MODID, "thin_straw_rope", Main.GROUP_ITEM);
		ModItems.smallCrucible = new ItemBase(Main.MODID, "small_crucible", Main.GROUP_ITEM);

		ModItems.gun = new ItemGun();

		ModItems.toolHammer = new MaterialToolItem(Main.MODID, "hammer", Main.GROUP_ITEM, ToolHammer.INSTANCE);
		ModItems.toolChisel = new MaterialToolItem(Main.MODID, "chisel", Main.GROUP_ITEM, ToolChisel.INSTANCE);
		ModItems.toolKnife = new MaterialToolItem(Main.MODID, "knife", Main.GROUP_ITEM, ToolKnife.INSTANCE);
		ModItems.toolJavelin = new MaterialToolItem(Main.MODID, "javelin", Main.GROUP_ITEM, ToolJavelin.INSTANCE);

		ModBlocks.coverItemStone = new BlockCoverItem("stone", Material.STONE, new ItemStack(ModItems.stone),
				Block.box(1, 0, 1, 15, 2, 15));
		ModBlocks.coverItemFlint = new BlockCoverItem("flint", Material.STONE, new ItemStack(Items.FLINT),
				Block.box(1, 0, 1, 15, 2, 15));
		ModBlocks.coverItemStick = new BlockCoverItem("stick", Material.WOOD, new ItemStack(Items.STICK),
				Block.box(1, 0, 1, 15, 2, 15));

		ModBlocks.coverGrass = new BlockCoverGrass();
		ModBlocks.stoneWorkTable = new BlockStoneWorkTable();
		ModBlocks.grindstone = new BlockGrindstone();
		ModBlocks.campfire = new BlockCampfire();

		SolidShape.REGISTRY.values().forEach((shape) -> {
			MaterialBase.REGISTRY.values().forEach((material) -> {
				if (shape.generateMaterial(material)) {
					new MaterialItem(shape, material);
				}
			});
		});
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