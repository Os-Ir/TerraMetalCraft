package com.osir.terrametalcraft.common.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.osir.terrametalcraft.Main;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockCoverItem extends Block {
	public static final Map<ItemStack, BlockCoverItem> COVER_ITEM_BLOCK = new HashMap<ItemStack, BlockCoverItem>();

	protected ItemStack stack;
	protected VoxelShape shape;

	public BlockCoverItem(String registryName, Material material, ItemStack stack, VoxelShape shape) {
		super(AbstractBlock.Properties.of(material).instabreak().sound(SoundType.WOOD).noOcclusion());
		this.setRegistryName(Main.MODID, "cover_item_" + registryName);
		this.stack = stack.copy();
		this.shape = shape;
		COVER_ITEM_BLOCK.put(this.stack, this);
		ModBlocks.REGISTERED_BLOCK.add(this);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Arrays.asList(this.stack.copy());
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit) {
		world.destroyBlock(pos, false);
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		ItemHandlerHelper.giveItemToPlayer(player, this.stack.copy());
		return ActionResultType.CONSUME;
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
			PlayerEntity player) {
		return this.stack.copy();
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
			boolean isMoving) {
		if (world.getBlockState(pos.below()).getBlock() == Blocks.AIR) {
			world.destroyBlock(pos, true);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shape;
	}
}