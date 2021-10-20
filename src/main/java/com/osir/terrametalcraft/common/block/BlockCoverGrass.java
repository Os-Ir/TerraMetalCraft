package com.osir.terrametalcraft.common.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.item.ModItems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockCoverGrass extends Block {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

	public BlockCoverGrass() {
		super(AbstractBlock.Properties.of(Material.PLANT).randomTicks().instabreak().sound(SoundType.CROP));
		this.setRegistryName(Main.MODID, "cover_grass");
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
		ModBlocks.REGISTERED_BLOCK.add(this);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < 7;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 1)) {
			return;
		}
		if (random.nextInt(4) == 0) {
			world.setBlock(pos, this.defaultBlockState().setValue(AGE, state.getValue(AGE) + 1), 2);
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(AGE) >= 7) {
			return Arrays.asList(new ItemStack(ModItems.hay));
		} else {
			return Arrays.asList(new ItemStack(ModItems.grass));
		}
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		if (state.getValue(AGE) >= 7) {
			world.destroyBlock(pos, false);
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.hay));
		}
		return ActionResultType.CONSUME;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}