package com.osir.terrametalcraft.common.item;

import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.github.zi_jing.cuckoolib.item.MaterialToolItem;
import com.github.zi_jing.cuckoolib.item.ToolItem;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.tool.ToolChisel;
import com.osir.terrametalcraft.api.tool.ToolHammer;
import com.osir.terrametalcraft.api.tool.ToolIgniter;
import com.osir.terrametalcraft.api.tool.ToolJavelin;
import com.osir.terrametalcraft.api.tool.ToolKnife;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

	public static final Item TMC_COIN = new ItemBase(Main.GROUP_ITEM);
	public static final Item STONE = new ItemBase(Main.GROUP_ITEM);
	public static final Item CHIPPED_STONE = new ItemBase(Main.GROUP_ITEM);
	public static final Item GRINDED_STONE = new ItemBase(Main.GROUP_ITEM);
	public static final Item CHIPPED_FLINT = new ItemBase(Main.GROUP_ITEM);
	public static final Item GRINDED_FLINT = new ItemBase(Main.GROUP_ITEM);
	public static final Item GRASS = new ItemBase(Main.GROUP_ITEM);
	public static final Item HAY = new ItemBase(Main.GROUP_ITEM);
	public static final Item LEAVES = new ItemBase(Main.GROUP_ITEM);
	public static final Item THIN_STRAW_ROPE = new ItemBase(Main.GROUP_ITEM);
	public static final ItemSmallCrucible SMALL_CRUCIBLE = new ItemSmallCrucible();

	public static final Item GUN = new ItemGun();

	public static final MaterialToolItem HAMMER = new MaterialToolItem(Main.GROUP_ITEM, ToolHammer.INSTANCE);
	public static final MaterialToolItem CHISEL = new MaterialToolItem(Main.GROUP_ITEM, ToolChisel.INSTANCE);
	public static final MaterialToolItem KNIFE = new MaterialToolItem(Main.GROUP_ITEM, ToolKnife.INSTANCE);
	public static final MaterialToolItem JAVELIN = new MaterialToolItem(Main.GROUP_ITEM, ToolJavelin.INSTANCE);
	public static final ToolItem IGNITER = new ToolItem(Main.GROUP_ITEM, ToolIgniter.INSTANCE);

	public static final BlockItem BLOCK_CAMPFIRE = new BlockItem(ModBlocks.CAMPFIRE, new Item.Properties().tab(Main.GROUP_EQUIPMENT));
	public static final BlockItem BLOCK_GRINDSTONE = new BlockItem(ModBlocks.GRINDSTONE, new Item.Properties().tab(Main.GROUP_EQUIPMENT));
	public static final BlockItem BLOCK_STONE_WORK_TABLE = new BlockItem(ModBlocks.STONE_WORK_TABLE, new Item.Properties().tab(Main.GROUP_EQUIPMENT));

	public static final RegistryObject<Item> _TMC_COIN = ITEMS.register("tmc_coin", () -> TMC_COIN);
	public static final RegistryObject<Item> _STONE = ITEMS.register("stone", () -> STONE);
	public static final RegistryObject<Item> _CHIPPED_STONE = ITEMS.register("chipped_stone", () -> CHIPPED_STONE);
	public static final RegistryObject<Item> _GRINDED_STONE = ITEMS.register("grinded_stone", () -> GRINDED_STONE);
	public static final RegistryObject<Item> _CHIPPED_FLINT = ITEMS.register("chipped_flint", () -> CHIPPED_FLINT);
	public static final RegistryObject<Item> _GRINDED_FLINT = ITEMS.register("grinded_flint", () -> GRINDED_FLINT);
	public static final RegistryObject<Item> _GRASS = ITEMS.register("grass", () -> GRASS);
	public static final RegistryObject<Item> _HAY = ITEMS.register("hay", () -> HAY);
	public static final RegistryObject<Item> _LEAVES = ITEMS.register("leaves", () -> LEAVES);
	public static final RegistryObject<Item> _THIN_STRAW_ROPE = ITEMS.register("thin_straw_rope", () -> THIN_STRAW_ROPE);
	public static final RegistryObject<Item> _SMALL_CRUCIBLE = ITEMS.register("small_crucible", () -> SMALL_CRUCIBLE);

	public static final RegistryObject<Item> _GUN = ITEMS.register("gun", () -> GUN);

	public static final RegistryObject<MaterialToolItem> _HAMMER = ITEMS.register("hammer", () -> HAMMER);
	public static final RegistryObject<MaterialToolItem> _CHISEL = ITEMS.register("chisel", () -> CHISEL);
	public static final RegistryObject<MaterialToolItem> _KNIFE = ITEMS.register("knife", () -> KNIFE);
	public static final RegistryObject<MaterialToolItem> _JAVELIN = ITEMS.register("javelin", () -> JAVELIN);
	public static final RegistryObject<Item> _IGNITER = ITEMS.register("igniter", () -> IGNITER);

	public static final RegistryObject<BlockItem> _BLOCK_CAMPFIRE = ITEMS.register("campfire", () -> BLOCK_CAMPFIRE);
	public static final RegistryObject<BlockItem> _BLOCK_GRINDSTONE = ITEMS.register("grindstone", () -> BLOCK_GRINDSTONE);
	public static final RegistryObject<BlockItem> _BLOCK_STONE_WORK_TABLE = ITEMS.register("stone_work_table", () -> BLOCK_STONE_WORK_TABLE);
}