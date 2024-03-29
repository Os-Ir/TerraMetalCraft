package com.osir.terrametalcraft.common.block;

import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.common.item.ModItems;
import com.osir.terrametalcraft.common.te.TECampfire;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCampfire extends Block {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

	public BlockCampfire() {
		super(AbstractBlock.Properties.of(Material.WOOD).strength(0.5f).sound(SoundType.WOOD).noOcclusion());
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		TECampfire te = (TECampfire) world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == ModItems.IGNITER) {
			IHeatable heat = stack.getCapability(ModCapabilities.HEATABLE).orElse(null);
			if (te.ignite(heat.getTemperature())) {
				heat.increaseEnergy(-40000);
			}
		} else {
			ModularGuiInfo.openModularGui(te, (ServerPlayerEntity) player);
		}
		return ActionResultType.CONSUME;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TECampfire();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}