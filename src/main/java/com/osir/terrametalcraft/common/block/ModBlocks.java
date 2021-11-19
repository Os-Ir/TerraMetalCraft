package com.osir.terrametalcraft.common.block;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

	public static final Block COVER_ITEM_STONE = new BlockCoverItem(Material.STONE, new ItemStack(ModItems.STONE), Block.box(1, 0, 1, 15, 2, 15));
	public static final Block COVER_ITEM_FLINT = new BlockCoverItem(Material.STONE, new ItemStack(Items.FLINT), Block.box(1, 0, 1, 15, 2, 15));
	public static final Block COVER_ITEM_STICK = new BlockCoverItem(Material.WOOD, new ItemStack(Items.STICK), Block.box(1, 0, 1, 15, 2, 15));

	public static final Block COVER_GRASS = new BlockCoverGrass();
	public static final Block STONE_WORK_TABLE = new BlockStoneWorkTable();
	public static final Block GRINDSTONE = new BlockGrindstone();
	public static final Block CAMPFIRE = new BlockCampfire();
	public static final Block POTTERY_WORK_TABLE = new BlockPotteryWorkTable();

	public static final RegistryObject<Block> _COVER_ITEM_STONE = BLOCKS.register("cover_item_stone", () -> COVER_ITEM_STONE);
	public static final RegistryObject<Block> _COVER_ITEM_FLINT = BLOCKS.register("cover_item_flint", () -> COVER_ITEM_FLINT);
	public static final RegistryObject<Block> _COVER_ITEM_STICK = BLOCKS.register("cover_item_stick", () -> COVER_ITEM_STICK);

	public static final RegistryObject<Block> _COVER_GRASS = BLOCKS.register("cover_grass", () -> COVER_GRASS);
	public static final RegistryObject<Block> _STONE_WORK_TABLE = BLOCKS.register("stone_work_table", () -> STONE_WORK_TABLE);
	public static final RegistryObject<Block> _GRINDSTONE = BLOCKS.register("grindstone", () -> GRINDSTONE);
	public static final RegistryObject<Block> _CAMPFIRE = BLOCKS.register("campfire", () -> CAMPFIRE);
}